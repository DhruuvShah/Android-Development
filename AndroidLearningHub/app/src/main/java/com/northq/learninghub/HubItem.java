package com.northq.learninghub;

/** Unit 2 practical concept applied: a simple POJO model backing a RecyclerView. */
public class HubItem {
    public final int iconResId;
    public final String title;
    public final String subtitle;
    public final Class<?> targetActivity;

    public HubItem(int iconResId, String title, String subtitle, Class<?> targetActivity) {
        this.iconResId = iconResId;
        this.title = title;
        this.subtitle = subtitle;
        this.targetActivity = targetActivity;
    }
}
