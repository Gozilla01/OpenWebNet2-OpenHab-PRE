package org.openwebnet.message;

import java.util.Arrays;

import org.openwebnet.OpenDeviceType;

public class AutomationExt extends BaseOpenMessage {
    private static final int g;

    protected AutomationExt(final String s) {
        super(s);
    }

    @Override
    protected final What a(final int n) {
        return WHAT.fromValue(n);
    }

    public static AutomationExt requestStop(final String s, final Type type) {
        // Correzione provvisoria per la problematica nel group con doppio con .replaceFirst("\\##", "\\#") ##
        // BaseOpenMessage.a(s, type);
        return new AutomationExt(String.format("*%d*%d*%s##", AutomationExt.g, WHAT.STOP.a,
                BaseOpenMessage.b(s, type).replaceFirst("\\##", "\\#")));
    }

    public static AutomationExt requestMoveUp(final String s, final Type type) {
        // Correzione provvisoria per la problematica nel group con doppio con .replaceFirst("\\##", "\\#") ##
        /// BaseOpenMessage.a(s, type);
        return new AutomationExt(String.format("*%d*%d*%s##", AutomationExt.g, WHAT.UP.a,
                BaseOpenMessage.b(s, type).replaceFirst("\\##", "\\#")));
    }

    public static AutomationExt requestMoveDown(final String s, final Type type) {
        // Correzione provvisoria per la problematica nel group con doppio con .replaceFirst("\\##", "\\#") ##
        // BaseOpenMessage.a(s, type);
        return new AutomationExt(String.format("*%d*%d*%s##", AutomationExt.g, WHAT.DOWN.a,
                BaseOpenMessage.b(s, type).replaceFirst("\\##", "\\#")));
    }

    public static AutomationExt requestStatus(final String s, final Type type) {
        // Correzione provvisoria per la problematica nel group con doppio con .replaceFirst("\\##", "\\#") ##
        // BaseOpenMessage.a(s, type);
        return new AutomationExt(
                String.format("*#%d*%s##", AutomationExt.g, BaseOpenMessage.b(s, type).replaceFirst("\\##", "\\#")));
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

    public enum WHAT implements What {
        STOP(Integer.valueOf(0)),
        UP(Integer.valueOf(1)),
        DOWN(Integer.valueOf(2)),
        COMMAND_TRANSLATION(Integer.valueOf(1000));

        private final Integer a;

        private WHAT(final Integer a) {
            this.a = a;
        }

        public static WHAT fromValue(final Integer n) {
            return Arrays.stream(values()).filter(what -> n == what.a).findFirst().orElseGet(null);
        }

        @Override
        public final Integer value() {
            return this.a;
        }
    }
}
