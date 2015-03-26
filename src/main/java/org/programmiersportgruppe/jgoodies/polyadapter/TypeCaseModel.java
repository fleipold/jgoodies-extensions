package org.programmiersportgruppe.jgoodies.polyadapter;


import com.jgoodies.binding.beans.Model;
import com.jgoodies.binding.value.AbstractValueModel;
import com.jgoodies.binding.value.ValueHolder;
import com.jgoodies.binding.value.ValueModel;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;

/**
 *  @author Felix Leipold
 *
 * The TypeCaseModel watches a ValueModel that over time holds objects of different classes
 * It transforms this model into different channels that are activated when the subject's
 * class changes. You have to get channels for java classes.
 * <p/>
 * This might be useful if you have a polymorhpic List. You can bind the selectionHolder to
 * a TypeCaseCodel and use the channels to feed different PresentationModels.
 * <p/>
 * The TypeCaseModel itself exposes a bound property activeChannelID, which contains the
 * ID associated with the currently active channel. It can e.g. be bound to a CardLayout
 * to switch a detail view/ editor.
 * <p/>
 * Furthermore there is an activity Model associated with each channel that holds a boolean
 * value indicating whether the channel is active.
 * <p/>
 * Please note that if the types associated with the different channels are not disjoint,
 * there can be more than one active channel at a time. If this is the case the
 * value of activeChannelID will be somewhat undefined.
 * <p/>
 * If a channel is inactive its value changes to a default value, that can be given (null or a
 * dedicated NullObject).
 * <p/>
 * <p/>
 * <h3>Example</h3>
 * <pre>
 * <p/>
 * TypeCaseModel typeCase=new TypeCaseModel(selectionInList.getSelection());
 * <p/>
 * PresentationModel classicModel=typeCase.getTypeChannel(ClassicAlbum.class,"classic");
 * PresentationModel rockModel==typeCase.getTypeChannel(RockAlbum.class,"rock");
 * <p/>
 *
 * </pre>
 * <p/>
 * User: Felix Leipold
 * Date: 07.10.2005
 * Time: 20:59:51
 */
public class TypeCaseModel extends Model {
    private Map activityModels;
    private ValueModel subject;

    private Object activeChannelID;

    /**
     * Creates a new TypeCaseModel
     *
     * @param subject the subject which is going to be demultiplexed to the channels
     */
    public TypeCaseModel(ValueModel subject) {
        this.subject = subject;
        activityModels = new HashMap();
    }


    /**
     * Creates a new TypeChannel for a given type
     *
     * @param javaType   the type associated with this channel. This channel gets activated
     *                   when the type of the value of subject changes to javaType or one of its subtypes.
     * @param nullObject the channel is set to this value if its inactive.
     * @param channelID  an identifier for this channel which is used by the activeChannelID Property
     *                   to indicate that this particular channel is active.
     * @return a ValueModel representing the TypeChannel
     */
    public ValueModel getTypeChannel(Class javaType, Object nullObject, Object channelID) {
        TypeChannel channel = new TypeChannel(javaType, nullObject, channelID);
        activityModels.put(channel, channel.getActive());
        return channel;
    }

    /**
     * Creates a new TypeChannel for a given type
     * The inactive channel points to null.
     *
     * @param javaType  the type associated with this channel. This channel gets activated
     *                  when the type of the value of subject changes to javaType or one of its subtypes.
     * @param channelID an identifier for this channel which is used by the activeChannelID Property
     *                  to indicate that this particular channel is active.
     * @return a ValueModel representing the TypeChannel
     */
    public ValueModel getTypeChannel(Class javaType, Object channelID) {
        return getTypeChannel(javaType, null, channelID);
    }

    /**
     * Creates a new TypeChannel for a given type
     * The inactive channel points to null. The fully qualified classname of javaType is
     * used as ChannelID.
     *
     * @param javaType the type associated with this channel. This channel gets activated
     *                 when the type of the value of subject changes to javaType or one of its subtypes.
     * @return a ValueModel representing the TypeChannel
     */

    public ValueModel getTypeChannel(Class javaType) {
        return getTypeChannel(javaType, null, javaType.getName());
    }


    /**
     * @param channel the TypeChannel in which we are interested
     * @return a ValueModel  holding a boolean value indicating the activity of the given channel
     */
    public ValueModel getActivityModel(ValueModel channel) {
        return (ValueModel) activityModels.get(channel);
    }

    /**
     * @return The ID associated with active channel
     *         note that this value is not properly defined if more than one channel
     *         is active at a time.
     */
    public Object getActiveChannelID() {
        return activeChannelID;
    }

    private void setActiveChannelID(Object activeChannelID) {
        Object old = this.activeChannelID;
        this.activeChannelID = activeChannelID;
        firePropertyChange("activeChannelID", old, activeChannelID);
    }


    /**
     * TypeChannels provide a ValueModel and Listen to the subject
     */
    private class TypeChannel extends AbstractValueModel implements PropertyChangeListener {
        private Class javaType;
        private Object channelID;
        private Object nullObject;

        private ValueHolder isActive;

        private TypeChannel(Class javaType, Object nullObject, Object channelID) {
            this.javaType = javaType;
            this.channelID = channelID;
            this.nullObject = nullObject;
            isActive = new ValueHolder(false);
            subject.addValueChangeListener(this);
            propertyChange(new PropertyChangeEvent(this, "value", null, subject.getValue()));
        }

        public Object getValue() {
            if (isActive() && (!javaType.isAssignableFrom(subject.getValue().getClass()) )){
                throw new RuntimeException("Assertion Broken, channel is active with wrong class");
            }
            if (isActive.booleanValue()) {
                return subject.getValue();
            } else {
                return nullObject;
            }
        }

        public void setValue(Object newValue) {
            throw new RuntimeException("Vague Semantics don't call this method");
        }

        /**
         * evaluates the type of the subject and sets activity and value accordingly
         */
        public void propertyChange(PropertyChangeEvent evt) {
            boolean activeBefore = isActive();
            if (subject.getValue() == null) { // null has no type
                isActive.setValue(false);
                if (activeBefore)
                    fireValueChange(evt.getOldValue(), nullObject);
                return;
            }
            if (javaType.isAssignableFrom(subject.getValue().getClass())) {
                isActive.setValue(true);
                if (activeBefore) {
                    fireValueChange(evt.getOldValue(), evt.getNewValue());
                } else {
                    fireValueChange(nullObject, evt.getNewValue());
                }

                setActiveChannelID(channelID);
                return;
            } else {
                isActive.setValue(false);
                if (activeBefore)
                    fireValueChange(evt.getOldValue(), nullObject);

            }
        }

        private ValueHolder getActive() {
            return isActive;
        }

        private boolean isActive() {
            return isActive.booleanValue();
        }

    }


}
