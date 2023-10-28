package com.dv.commons.base;

import java.io.Serializable;
import java.util.Locale;

public interface BaseStatus extends BaseEnum<Serializable> {

    String message(Locale locale,Object... args);

    String message();
}
