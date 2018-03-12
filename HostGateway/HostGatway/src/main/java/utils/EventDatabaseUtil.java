package com.lexi.comp3601.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.lexi.comp3601.data.Event;
import com.lexi.comp3601.data.EventListItem;
import com.lexi.comp3601.data.Profile;
import com.lexi.comp3601.database.HibernateDatabaseEventManager;
import com.lexi.comp3601.utils.Variables.EventParams;
import com.lexi.comp3601.utils.Variables.NotificationType;

public class EventDatabaseUtil {

	public static boolean eventExists(int id) {
		return HibernateDatabaseTransactionManager.getDefault().eventExists(id);
	}

	public static boolean update(Event event) {
		return HibernateDatabaseTransactionManager.getDefault().update(event);
	}

	public static Event addEvent(String username, Map<String, Object> params) {
		Profile host = null;
		Event event = null;

		host = UserDatabaseUtil.getUser(username);
		if (host == null) {
			return null;
		}

		try {
			event = new Event(generateID(), username, TimeUtil.now(), params.get(EventParams.GAME).toString());

			event.setCreatorIcon(host.getIcon());
			event.setLookingFor(params.get(EventParams.LOOKING).toString());
			event.setNextGame(params.get(EventParams.NEXT).toString());
			event.setMaxPlayers((int) params.get(EventParams.MAX));
			event.setFrequency(params.get(EventParams.FREQUENCY).toString());
			event.setOccurrences((int) params.get(EventParams.OCCURRENCES));
			event.setLanguage(params.get(EventParams.LANG).toString());
			event.setNewPlayers((boolean) params.get(EventParams.NEW));
			event.setMatureContent((boolean) params.get(EventParams.MATURE));
			event.setDescription(params.get(EventParams.DESC).toString());

			if (HibernateDatabaseTransactionManager.getDefault().add(event)) {
				host.addHostedEvent(event.getID());
				if (UserDatabaseUtil.updateProfile(host)) {
					return event;
				}
			}
		} catch (Exception e) {
			return null;
		}
		return null;
	}

	public static boolean removeEvent(int id) {
		if (!eventExists(id)) {
			return false;
		}
		Event event = getEvent(id);

		Profile creator = UserDatabaseUtil.getUser(event.getCreator());
		creator.removeHostedEvent(id);
		UserDatabaseUtil.updateProfile(creator);

		for (String joinee : event.getPlayers().keySet()) {
			Profile p = UserDatabaseUtil.getUser(joinee);
			p.removeEventJoined(id);
			UserDatabaseUtil.updateProfile(p);
			NotificationDatabaseUtil.addNotification(NotificationType.REMOVED, p.getUserid(), event.getID());
		}
		return HibernateDatabaseTransactionManager.getDefault().delete(id);
	}

	public static Event getEvent(int id) {
		return HibernateDatabaseTransactionManager.getDefault().getEventWithID(id);
	}

	public static List<EventListItem> getHostedEvents(String userid) {
		Profile profile = UserDatabaseUtil.getUser(userid);
		if (profile == null) {
			return null;
		}
		List<Integer> events = profile.getHostedEvents();
		if (events == null || events.isEmpty()) {
			return null;
		}
		List<EventListItem> userEvents = new ArrayList<EventListItem>();
		for (int id : events) {
			Event e = getEvent(id);
			if (e == null) {
				continue;
			}
			userEvents.add(new EventListItem(e));
		}
		return userEvents;
	}

	public static List<EventListItem> getEventsIn(String userid) {
		Profile profile = UserDatabaseUtil.getUser(userid);
		if (profile == null) {
			return null;
		}
		List<Integer> events = profile.getEventsJoined();
		if (events == null || events.isEmpty()) {
			return null;
		}
		List<EventListItem> userEvents = new ArrayList<EventListItem>();
		for (int id : events) {
			Event e = getEvent(id);
			if (e == null) {
				continue;
			}
			userEvents.add(new EventListItem(e));
		}
		return userEvents;
	}

	public static List<EventListItem> getEventsWithTitle(String title) {
		List<Event> events = HibernateDatabaseTransactionManager.getDefault().searchEventsWithTitle(title);
		if (events == null || events.isEmpty()) {
			return null;
		}

		List<EventListItem> eventList = new ArrayList<EventListItem>();
		for (Event e : events) {
			eventList.add(new EventListItem(e));
		}
		return eventList;
	}
	
	public static List<EventListItem> getEvents(Map<String, Object> params) {
		List<Event> events = new ArrayList<Event>();
		if (params == null || params.isEmpty()) {
			events = HibernateDatabaseTransactionManager.getDefault().getAllEvents();
		} else {
			Map<String, Object> approvedKeys = new HashMap<String, Object>();
			for (String key : params.keySet()) {
				if (Variables.eventParams.contains(key)) {
					approvedKeys.put(key, params.get(key));
				}
			}
			if (approvedKeys == null || approvedKeys.isEmpty()) {
				return null;
			}
			events = HibernateDatabaseTransactionManager.getDefault().searchEvents(approvedKeys);
		}

		if (events == null || events.isEmpty()) {
			return null;
		}

		List<EventListItem> userEvents = new ArrayList<EventListItem>();
		for (Event e : events) {
			userEvents.add(new EventListItem(e));
		}
		return userEvents;
	}

	public static boolean ownsEvent(int id, String user) {
		return HibernateDatabaseTransactionManager.getDefault().ownsEvent(id, user);
	}

	public static boolean joinEvent(int id, String user) {
		Profile joining = UserDatabaseUtil.getUser(user);
		if (joining == null) {
			return false;
		} else if (ownsEvent(id, user)) {
			return false;
		}

		Event event = getEvent(id);
		if (event == null) {
			return false;
		} else if (event.getMaxPlayers() == event.numOfPlayers()) {
			return false;
		} else if (event.containsUser(user)) {
			return true;
		}

		joining.addEventJoined(event.getID());
		event.addPlayer(joining.getUserid());

		if (!HibernateDatabaseTransactionManager.getDefault().update(event)) {
			return false;
		}
		NotificationDatabaseUtil.addNotification(NotificationType.JOINED, event.getCreator(), joining.getUserid());
		return UserDatabaseUtil.updateProfile(joining);
	}

	public static boolean approveJoin(int id, String owner, String joinee, boolean approve) {
		if (!eventExists(id)) {
			return false;
		} else if (!ownsEvent(id, owner)) {
			return false;
		}

		Event event = getEvent(id);
		if (!event.containsUser(joinee)) {
			return false;
		}

		if (approve) {
			event.approvePlayer(joinee);
			NotificationDatabaseUtil.addNotification(NotificationType.APPROVED, joinee, owner);
		} else {
			event.disapprovePlayer(joinee);
			Profile profile = UserDatabaseUtil.getUser(joinee);
			if (profile != null) {
				profile.removeEventJoined(id);
				UserDatabaseUtil.updateProfile(profile);
			}
			NotificationDatabaseUtil.addNotification(NotificationType.DISAPPROVED, joinee, event.getID());
		}
		return HibernateDatabaseTransactionManager.getDefault().update(event);
	}

	private static int generateID() {
		Random rnd = new Random();
		int id = 0;
		do {
			id = 100000 + rnd.nextInt(900000);
		} while (eventExists(id));
		return id;
	}

}
