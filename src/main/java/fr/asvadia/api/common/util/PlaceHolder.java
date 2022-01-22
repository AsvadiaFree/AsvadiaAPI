package fr.asvadia.api.common.util;

public class PlaceHolder {

    static Character tag = '%';

    String key;
    String value;

    public PlaceHolder(String key) {
        this(key, "");
    }

    public PlaceHolder(String key, String value) {
        this.key = key;
        if(value == null) this.value = "?";
        else this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static String replace(String element, PlaceHolder... placeHolders) {
        for (PlaceHolder placeHolder : placeHolders) {
            if (placeHolder.getValue() != null)
                element = element.replaceAll(getStringTag() + placeHolder.getKey() + getStringTag(), placeHolder.getValue());
        }
        return element;
    }

    public static void setTag(Character tag) {
        PlaceHolder.tag = tag;
    }

    public static Character getTag() {
        return tag;
    }

    private static String getStringTag() {
        return Character.toString(tag);
    }




}
