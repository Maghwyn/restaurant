package com.codingfactory.restaurant.interfaces;

/**
 * Interface ControllerInterface used to inject the proper type of
 * the controller to a component used in a modal
 * @param <T> The controllerType that will be used to type the injected controller
 */
public interface ControllerInterface<T> {
    void setParentController(T controller);
}
