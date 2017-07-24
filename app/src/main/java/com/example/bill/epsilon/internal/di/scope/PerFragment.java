package com.example.bill.epsilon.internal.di.scope;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import javax.inject.Scope;

@Scope
@Retention(RUNTIME)
public @interface PerFragment {

}
