package org.openwebnet.message;

import java.util.Arrays;
import org.openwebnet.OpenDeviceType;

public class Automation extends BaseOpenMessage
{
    private static final int g;
    
    protected Automation(final String s) {
        super(s);
    }
    
    @Override
    protected final What a(final int n) {
        return WHAT.fromValue(n);
    }
    
    public static Automation requestStop(final String s, final Type type) {
        BaseOpenMessage.a(s, type);
        return new Automation(String.format("*%d*%d*%s##", Automation.g, WHAT.STOP.a, BaseOpenMessage.b(s, type)));
    }
    
    public static Automation requestMoveUp(final String s, final Type type) {
        BaseOpenMessage.a(s, type);
        return new Automation(String.format("*%d*%d*%s##", Automation.g, WHAT.UP.a, BaseOpenMessage.b(s, type)));
    }
    
    public static Automation requestMoveDown(final String s, final Type type) {
        BaseOpenMessage.a(s, type);
        return new Automation(String.format("*%d*%d*%s##", Automation.g, WHAT.DOWN.a, BaseOpenMessage.b(s, type)));
    }
    
    public static Automation requestStatus(final String s, final Type type) {
        BaseOpenMessage.a(s, type);
        return new Automation(String.format("*#%d*%s##", Automation.g, BaseOpenMessage.b(s, type)));
    }
    
    public boolean isUp() {
        return this.getWhat() != null && this.getWhat().equals(WHAT.UP);
    }
    
    public boolean isDown() {
        return this.getWhat() != null && this.getWhat().equals(WHAT.DOWN);
    }
    
    public boolean isStop() {
        return this.getWhat() != null && this.getWhat().equals(WHAT.STOP);
    }
    
    @Override
    public OpenDeviceType detectDeviceType() {
        if (this.isCommand()) {
            return OpenDeviceType.SCS_SHUTTER_CONTROL;
        }
        return null;
    }
    
    static {
        g = Who.AUTOMATION.value();
    }
    
    public enum WHAT implements What
    {
        STOP(Integer.valueOf(0)), 
        UP(Integer.valueOf(1)), 
        DOWN(Integer.valueOf(2)), 
        COMMAND_TRANSLATION(Integer.valueOf(1000));
        
        private final Integer a;
        
        private WHAT(final Integer a) {
            this.a = a;
        }
        
        public static WHAT fromValue(final Integer n) {
            return Arrays.stream(values()).filter(what -> n == (int)what.a).findFirst().orElse(null);
        }
        
        @Override
        public final Integer value() {
            return this.a;
        }
    }
}
