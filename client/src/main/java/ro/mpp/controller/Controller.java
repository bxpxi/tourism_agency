package ro.mpp.controller;

import ro.mpp.IAppService;

public class Controller {
    IAppService appService;

    public IAppService getAppService() {
        return appService;
    }

    public void setAppService(IAppService appService) {
        this.appService = appService;
    }

    public static void showException(Exception e) {
        Utils.showErrorBox(e.getMessage());
    }
}