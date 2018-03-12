package database;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import data.Profile;

public class ProfileDatabaseTest {

	@Test
	public void testOneProfile() {
		Profile p = new Profile("user123", "password123");

		assertTrue(HibernateDatabaseProfileManager.getDefault().setupTable());

		assertTrue(HibernateDatabaseProfileManager.getDefault().add(p));

		System.out.println(
				HibernateDatabaseProfileManager.getDefault().getProfileWithName("user123").getPrimaryKey());
		System.out.println(p.getPrimaryKey());

		assertTrue(HibernateDatabaseProfileManager.getDefault().getProfileWithName("user123").getClass()
				.equals(p.getClass()));

		assertTrue(HibernateDatabaseProfileManager.getDefault().delete(p));

		assertFalse(HibernateDatabaseProfileManager.getDefault().profileExists("user123"));
	}
	/*
	
	@Test
	public void testOneProfileWithSettings() {
		String status = "This is a status";
		Profile p = new Profile("user12345", "password123");
		p.setBackground(3);
		p.setIcon(2);
		p.setStatus(status);

		assertTrue(HibernateDatabaseProfileManager.getDefault().setupTable());

		assertTrue(HibernateDatabaseProfileManager.getDefault().add(p));

		System.out.println(
				HibernateDatabaseProfileManager.getDefault().getProfileWithName("user12345").getPrimaryKey());
		System.out.println(p.getPrimaryKey());

		Profile profile = HibernateDatabaseProfileManager.getDefault().getProfileWithName("user12345");
		
		assertEquals(3, profile.getBackground());
		assertEquals(2, profile.getIcon());
		assertEquals(status, profile.getStatus());

		assertEquals(0, profile.getFollowers().size());
		assertEquals(0, profile.getFollowing().size());

		assertTrue(HibernateDatabaseProfileManager.getDefault().delete(p));

		assertFalse(HibernateDatabaseProfileManager.getDefault().profileExists("user12345"));
	}
	
	public void testOneProfileWithFollowers() {
		Profile p = new Profile("user1234567", "password123");
		p.addFollower("user1");
		p.addFollower("user2");
		p.addFollower("user3");

		p.addFollowing("user1");
		p.addFollowing("user2");
		
		assertTrue(HibernateDatabaseProfileManager.getDefault().setupTable());

		assertTrue(HibernateDatabaseProfileManager.getDefault().add(p));

		System.out.println(
				HibernateDatabaseProfileManager.getDefault().getProfileWithName("user1234567").getPrimaryKey());
		System.out.println(p.getPrimaryKey());

		Profile profile = HibernateDatabaseProfileManager.getDefault().getProfileWithName("user1234567");
		
		assertEquals(3, profile.getFollowers().size());
		assertEquals(2, profile.getFollowing().size());

		assertTrue(profile.hasFollower("user1"));
		assertTrue(profile.hasFollower("user2"));
		assertTrue(profile.hasFollower("user3"));
		
		assertTrue(profile.isFollowing("user1"));
		assertTrue(profile.isFollowing("user2"));
		assertFalse(profile.isFollowing("user3"));
		
		assertTrue(HibernateDatabaseProfileManager.getDefault().delete(p));

		assertFalse(HibernateDatabaseProfileManager.getDefault().profileExists("user1234567"));
	}
	
	@Test
	public void testMultipleProfiles() {
		// make 5 profiles
		Profile p1 = new Profile("user1", "Pass1");
		Profile p2 = new Profile("user2", "Pass2");
		Profile p3 = new Profile("user3", "Pass3");
		Profile p4 = new Profile("user4", "Pass4");
		Profile p5 = new Profile("user5", "Pass5");

		assertTrue(HibernateDatabaseProfileManager.getDefault().setupTable());

		// add one profile
		assertTrue(HibernateDatabaseProfileManager.getDefault().add(p1));

		// error duplicate usernames
		assertFalse(HibernateDatabaseProfileManager.getDefault().add(p1));
		
		// add four more profiles
		assertTrue(HibernateDatabaseProfileManager.getDefault().add(p2));
		assertTrue(HibernateDatabaseProfileManager.getDefault().add(p3));
		assertTrue(HibernateDatabaseProfileManager.getDefault().add(p4));
		assertTrue(HibernateDatabaseProfileManager.getDefault().add(p5));		
		
		// check all profiles exists by username, and by username and pasword
		assertTrue(HibernateDatabaseProfileManager.getDefault().profileExists(p1.getUserid()));
		assertTrue(HibernateDatabaseProfileManager.getDefault().profileExists(p1.getUserid(), p1.getPassword()));
		
		assertTrue(HibernateDatabaseProfileManager.getDefault().profileExists(p2.getUserid()));
		assertTrue(HibernateDatabaseProfileManager.getDefault().profileExists(p2.getUserid(), p2.getPassword()));
		
		assertTrue(HibernateDatabaseProfileManager.getDefault().profileExists(p3.getUserid()));
		assertTrue(HibernateDatabaseProfileManager.getDefault().profileExists(p3.getUserid(), p3.getPassword()));
		
		assertTrue(HibernateDatabaseProfileManager.getDefault().profileExists(p4.getUserid()));
		assertTrue(HibernateDatabaseProfileManager.getDefault().profileExists(p4.getUserid(), p4.getPassword()));
		
		assertTrue(HibernateDatabaseProfileManager.getDefault().profileExists(p5.getUserid()));
		assertTrue(HibernateDatabaseProfileManager.getDefault().profileExists(p5.getUserid(), p5.getPassword()));

		// delete one profile
		assertTrue(HibernateDatabaseProfileManager.getDefault().delete(p1.getUserid()));

		// check profile doesn't exist anymore
		assertFalse(HibernateDatabaseProfileManager.getDefault().profileExists(p1.getUserid()));
		assertFalse(HibernateDatabaseProfileManager.getDefault().profileExists(p1.getUserid(), p1.getPassword()));
		
		assertTrue(HibernateDatabaseProfileManager.getDefault().profileExists(p2.getUserid()));
		assertTrue(HibernateDatabaseProfileManager.getDefault().profileExists(p2.getUserid(), p2.getPassword()));
		
		assertTrue(HibernateDatabaseProfileManager.getDefault().profileExists(p3.getUserid()));
		assertTrue(HibernateDatabaseProfileManager.getDefault().profileExists(p3.getUserid(), p3.getPassword()));
		
		assertTrue(HibernateDatabaseProfileManager.getDefault().profileExists(p4.getUserid()));
		assertTrue(HibernateDatabaseProfileManager.getDefault().profileExists(p4.getUserid(), p4.getPassword()));
		
		assertTrue(HibernateDatabaseProfileManager.getDefault().profileExists(p5.getUserid()));
		assertTrue(HibernateDatabaseProfileManager.getDefault().profileExists(p5.getUserid(), p5.getPassword()));

		// delete a profile
		assertTrue(HibernateDatabaseProfileManager.getDefault().delete(p2.getUserid()));

		assertFalse(HibernateDatabaseProfileManager.getDefault().profileExists(p1.getUserid()));
		assertFalse(HibernateDatabaseProfileManager.getDefault().profileExists(p1.getUserid(), p1.getPassword()));
		
		assertFalse(HibernateDatabaseProfileManager.getDefault().profileExists(p2.getUserid()));
		assertFalse(HibernateDatabaseProfileManager.getDefault().profileExists(p2.getUserid(), p2.getPassword()));
		
		assertTrue(HibernateDatabaseProfileManager.getDefault().profileExists(p3.getUserid()));
		assertTrue(HibernateDatabaseProfileManager.getDefault().profileExists(p3.getUserid(), p3.getPassword()));
		
		assertTrue(HibernateDatabaseProfileManager.getDefault().profileExists(p4.getUserid()));
		assertTrue(HibernateDatabaseProfileManager.getDefault().profileExists(p4.getUserid(), p4.getPassword()));
		
		assertTrue(HibernateDatabaseProfileManager.getDefault().profileExists(p5.getUserid()));
		assertTrue(HibernateDatabaseProfileManager.getDefault().profileExists(p5.getUserid(), p5.getPassword()));

		// delete a profile
		assertTrue(HibernateDatabaseProfileManager.getDefault().delete(p3.getUserid()));

		assertFalse(HibernateDatabaseProfileManager.getDefault().profileExists(p1.getUserid()));
		assertFalse(HibernateDatabaseProfileManager.getDefault().profileExists(p1.getUserid(), p1.getPassword()));
		
		assertFalse(HibernateDatabaseProfileManager.getDefault().profileExists(p2.getUserid()));
		assertFalse(HibernateDatabaseProfileManager.getDefault().profileExists(p2.getUserid(), p2.getPassword()));
		
		assertFalse(HibernateDatabaseProfileManager.getDefault().profileExists(p3.getUserid()));
		assertFalse(HibernateDatabaseProfileManager.getDefault().profileExists(p3.getUserid(), p3.getPassword()));
		
		assertTrue(HibernateDatabaseProfileManager.getDefault().profileExists(p4.getUserid()));
		assertTrue(HibernateDatabaseProfileManager.getDefault().profileExists(p4.getUserid(), p4.getPassword()));
		
		assertTrue(HibernateDatabaseProfileManager.getDefault().profileExists(p5.getUserid()));
		assertTrue(HibernateDatabaseProfileManager.getDefault().profileExists(p5.getUserid(), p5.getPassword()));

		// delete a profile
		assertTrue(HibernateDatabaseProfileManager.getDefault().delete(p4.getUserid()));

		assertFalse(HibernateDatabaseProfileManager.getDefault().profileExists(p1.getUserid()));
		assertFalse(HibernateDatabaseProfileManager.getDefault().profileExists(p1.getUserid(), p1.getPassword()));
		
		assertFalse(HibernateDatabaseProfileManager.getDefault().profileExists(p2.getUserid()));
		assertFalse(HibernateDatabaseProfileManager.getDefault().profileExists(p2.getUserid(), p2.getPassword()));
		
		assertFalse(HibernateDatabaseProfileManager.getDefault().profileExists(p3.getUserid()));
		assertFalse(HibernateDatabaseProfileManager.getDefault().profileExists(p3.getUserid(), p3.getPassword()));
		
		assertFalse(HibernateDatabaseProfileManager.getDefault().profileExists(p4.getUserid()));
		assertFalse(HibernateDatabaseProfileManager.getDefault().profileExists(p4.getUserid(), p4.getPassword()));
		
		assertTrue(HibernateDatabaseProfileManager.getDefault().profileExists(p5.getUserid()));
		assertTrue(HibernateDatabaseProfileManager.getDefault().profileExists(p5.getUserid(), p5.getPassword()));

		// delete a profile
		assertTrue(HibernateDatabaseProfileManager.getDefault().delete(p5.getUserid()));

		assertFalse(HibernateDatabaseProfileManager.getDefault().profileExists(p1.getUserid()));
		assertFalse(HibernateDatabaseProfileManager.getDefault().profileExists(p1.getUserid(), p1.getPassword()));
		
		assertFalse(HibernateDatabaseProfileManager.getDefault().profileExists(p2.getUserid()));
		assertFalse(HibernateDatabaseProfileManager.getDefault().profileExists(p2.getUserid(), p2.getPassword()));
		
		assertFalse(HibernateDatabaseProfileManager.getDefault().profileExists(p3.getUserid()));
		assertFalse(HibernateDatabaseProfileManager.getDefault().profileExists(p3.getUserid(), p3.getPassword()));
		
		assertFalse(HibernateDatabaseProfileManager.getDefault().profileExists(p4.getUserid()));
		assertFalse(HibernateDatabaseProfileManager.getDefault().profileExists(p4.getUserid(), p4.getPassword()));
		
		assertFalse(HibernateDatabaseProfileManager.getDefault().profileExists(p5.getUserid()));
		assertFalse(HibernateDatabaseProfileManager.getDefault().profileExists(p5.getUserid(), p5.getPassword()));	
	}
	*/
	
}
