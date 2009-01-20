package ws.leipold.felix.jgoodies.actions;

import com.jgoodies.binding.beans.PropertyConnector;
import com.jgoodies.binding.value.ValueModel;
import org.apache.commons.lang.StringUtils;

import javax.swing.Action;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import ws.leipold.felix.jgoodies.trigger.TriggerListener;
import ws.leipold.felix.jgoodies.actions.AdvancedAction;


public class ActionReflector {
    public ActionReflector(ActionFactory actionFactory) {
        this.actionFactory = actionFactory;
    }

    //This is probably not needed one we have a proper way of decorating the presentation model..
    public ActionReflector() {
        this (new ActionFactory(){
            public AdvancedAction createAction(String name) {
                return new AdvancedAction(name);
            }
        });
    }

    public  interface ActionFactory{
        public AdvancedAction createAction(String name);
    }

    private final ActionFactory actionFactory;


    public static void reflect(Object presentationModel) {
        new ActionReflector().reflectActions(presentationModel);
    }

    public static void reflect(Object presentationModel, ActionFactory actionFactory) {
        new ActionReflector(actionFactory).reflectActions(presentationModel);
    }

    private void reflectActions(Object presentationModel) {
        for (Field field : presentationModel.getClass().getDeclaredFields()) {
            if (!Action.class.isAssignableFrom(field.getType())) {
                continue;
            }
            reflectAction(presentationModel, field);
        }

    }

    private  void reflectAction(final Object presentationModel, Field field) {
        field.setAccessible(true);
        try {
            if (field.get(presentationModel) != null) return;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        final String actionName = field.getName();
        final Method actionMethod = reflectHandler(presentationModel, actionName);

        final ValueModel enabled = reflectEnabled(presentationModel, actionName);

        AdvancedAction a =  actionFactory.createAction(actionName);


        a.getActionTrigger().addTiggerListener(new MagicAction(actionMethod, presentationModel));

        if (enabled != null) {
            PropertyConnector.connectAndUpdate(enabled, a, "enabled");
        }
        try {
            field.set(presentationModel, a);
        }
        catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private  ValueModel reflectEnabled(Object presentationModel, String actionName) {
        try {
            Method getter = presentationModel.getClass().getMethod("can" + StringUtils.capitalize(actionName));
            return (ValueModel) getter.invoke(presentationModel);
        } catch (NoSuchMethodException e) {
            return null;
        } catch (IllegalAccessException e) {
            return null  ;
        } catch (InvocationTargetException e) {
             return null;
        }
    }

    private  Method reflectHandler(Object presentationModel, String actionName) {
        try {
            return presentationModel.getClass().getMethod(actionName);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("could not find handler for method ", e);
        }
    }

    private static class MagicAction implements TriggerListener {
        private final Method actionMethod;
        private final Object presentationModel;

        public MagicAction(Method actionMethod, Object presentationModel) {

            this.actionMethod = actionMethod;
            this.presentationModel = presentationModel;
        }


        public void handleTrigger() {
             try {
                actionMethod.invoke(presentationModel);
            } catch (IllegalAccessException e1) {
                throw new RuntimeException(e1);
            } catch (InvocationTargetException e1) {
                throw new RuntimeException(e1);
            }
        }
    }
}