package gdp;

import solutions.api.db.Entry;


/***
 * Immutable implementation of GdpFredEntry.
 */
public class GdpFredEntry implements Entry {
    private String date;
    private float value = 0f;

    public GdpFredEntry(String date) {
        this.date = date;
    }

    public GdpFredEntry(String date, float value) {
        this.date = date;
        this.value = value;
    }

    public float getValue() {
        return value;
    }

    public String getDate() {
        return date;
    }
}
