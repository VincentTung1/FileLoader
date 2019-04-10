package com.vincent.loadfilelibrary.topbar;

public class NavigationBarLayoutBean {

    private NavigationBarBean bean;

    private NavigationBar.Location location;

    private NavigationBar.Style style;

    private boolean isCenterLayout;

    public NavigationBarLayoutBean() {}

    public NavigationBarLayoutBean(NavigationBarBean bean, NavigationBar.Location location, NavigationBar.Style style, boolean isCenterLayout) {
        this.bean = bean;
        this.location = location;
        this.style = style;
        this.isCenterLayout = isCenterLayout;
    }

    public NavigationBarBean getBean() {
        return bean;
    }

    public void setBean(NavigationBarBean bean) {
        this.bean = bean;
    }

    public NavigationBar.Location getLocation() {
        return location;
    }

    public void setLocation(NavigationBar.Location location) {
        this.location = location;
    }

    public NavigationBar.Style getStyle() {
        return style;
    }

    public void setStyle(NavigationBar.Style style) {
        this.style = style;
    }

    public boolean isCenterLayout() {
        return isCenterLayout;
    }

    public void setCenterLayout(boolean centerLayout) {
        isCenterLayout = centerLayout;
    }
}
