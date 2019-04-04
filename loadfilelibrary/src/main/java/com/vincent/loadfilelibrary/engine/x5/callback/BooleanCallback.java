package com.vincent.loadfilelibrary.engine.x5.callback;

public interface BooleanCallback {

    void onSuccess(boolean isOK);

    void onError(Throwable e);
}
