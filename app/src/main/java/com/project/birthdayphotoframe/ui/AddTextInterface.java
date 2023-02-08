package com.project.birthdayphotoframe.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;

import java.io.Serializable;

public interface AddTextInterface  {

    void addFont(Typeface typeface);

    void addStickers(String path);

    void changeFrame(String path);

}
