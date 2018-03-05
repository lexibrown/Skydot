package com.comp.lexi.skydot.utils;

import com.comp.lexi.skydot.R;

public class IconUtil {

    private static int default_icon = R.mipmap.default_icon;

    private static Integer[] icons = {
            R.mipmap.anonymous2_m, R.mipmap.anonymous2_f, R.mipmap.anonymous_m,
            R.mipmap.anonymous_f, R.mipmap.person_m, R.mipmap.person_f,
            R.mipmap.person2_m, R.mipmap.person2_f, R.mipmap.elegant_m,
            R.mipmap.elegant_f, R.mipmap.glasses_m, R.mipmap.glasses_f,
            R.mipmap.agent_m, R.mipmap.agent_f, R.mipmap.cool_m,
    };

    private static String[] iconName = {
            "Dark Default Male", "Dark Default Female", "Default Female",
            "Default Male", "Male", "Female",
            "Male", "Female", "Elegant Male",
            "Elegant Female", "Glasses Male", "Glasses Female",
            "Agent Male", "Agent Female", "Cool Guy"
    };

    public static int getDefaultIcon() {
        return default_icon;
    }

    public static int getIcon(int i) {
        if (i < 0 || i >= icons.length) {
            return default_icon;
        }
        return icons[i];
    }

    public static String getIconName(int i) {
        // TODO
        return "";
//        if (i < 0 || i >= icons.length) {
//            return iconName[0];
//        }
//        return iconName[i];
    }

    public static int getNumIcons() {
        return icons.length;
    }

    public static int getBackground(int b) {
        switch (b) {
            case 1:
                return R.drawable.dnd_starter_set_art;
            default:
                return R.drawable.dnd_starter_set_art;
        }
    }

}
