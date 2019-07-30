package org.openwebnet.message;

import java.util.Arrays;

import org.openwebnet.OpenDeviceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LightingExt extends BaseOpenMessage {
    private static final Logger g;
    private static final int h;
    public static final int DIM_DIMMER_LEVEL_100 = 1;
    public static final int DIMMER_LEVEL_100_OFF = 100;
    public static final int DIMMER_LEVEL_100_MAX = 200;

    protected LightingExt(final String s) {
        super(s);
    }

    @Override
    protected final What a(final int n) {
        return WHAT.fromValue(n);
    }

    @Deprecated
    public static LightingExt requestTurnOn(final String s) {
        BaseOpenMessage.a(0, 9999, BaseOpenMessage.b(s));
        return new LightingExt(String.format("*%d*%d*%s##", LightingExt.h, 1, s));
    }

    public static LightingExt requestTurnOn(final String s, final Type type) {
        // Correzione provvisoria per la problematica nel group con doppio ##
        // BaseOpenMessage.a(s, type);
        return new LightingExt(
                String.format("*%d*%d*%s##", LightingExt.h, 1, BaseOpenMessage.b(s, type).replaceFirst("\\##", "\\#")));
    }

    @Deprecated
    public static LightingExt requestTurnOff(final String s) {
        BaseOpenMessage.a(0, 9999, BaseOpenMessage.b(s));
        return new LightingExt(String.format("*%d*%d*%s##", LightingExt.h, 0, s));
    }

    public static LightingExt requestTurnOff(final String s, final Type type) {
        // Correzione provvisoria per la problematica nel group con doppio ##
        // BaseOpenMessage.a(s, type);
        return new LightingExt(
                String.format("*%d*%d*%s##", LightingExt.h, 0, BaseOpenMessage.b(s, type).replaceFirst("\\##", "\\#")));
    }

    public static LightingExt requestDimTo(final String s, final What what, final Type type) {
        BaseOpenMessage.a(s, type);
        return new LightingExt(String.format("*%d*%d*%s##", LightingExt.h, what.value(), BaseOpenMessage.b(s, type)));
    }

    public static LightingExt requestDimToPercent(final String s, final int n, final Type type) {
        BaseOpenMessage.a(s, type);
        final What percentToWhat = percentToWhat(n);
        LightingExt.g.debug("##openwebnet## dimmer {} --> {}", n, percentToWhat);
        return new LightingExt(String.format("*%d*%d*%s##", LightingExt.h, percentToWhat, BaseOpenMessage.b(s, type)));
    }

    @Deprecated
    public static LightingExt requestStatus(final String s) {
        BaseOpenMessage.a(0, 9999, BaseOpenMessage.b(s));
        return new LightingExt(String.format("*#%d*%s##", LightingExt.h, s));
    }

    public static LightingExt requestStatus(final String s, final Type type) {
        // Correzione provvisoria per la problematica nel group con doppio ##
        // BaseOpenMessage.a(s, type);
        return new LightingExt(String.format("*#%d*%s##", LightingExt.h, BaseOpenMessage.b(s, type)));
    }

    public boolean isOn() {
        return this.getWhat() != null && this.getWhat().equals(WHAT.ON);
    }

    public boolean isOff() {
        return this.getWhat() != null && this.getWhat().equals(WHAT.OFF);
    }

    public boolean isMovement() {
        return this.getWhat() != null && this.getWhat().equals(WHAT.MOVEMENT_DETECTED);
    }

    public boolean isEndMovement() {
        return this.getWhat() != null && this.getWhat().equals(WHAT.END_MOVEMENT_DETECTED);
    }

    public static What percentToWhat(int n) {
        BaseOpenMessage.a(0, 100, n);
        if ((n = n) > 0 && n < 10) {
            n = 2;
        } else if ((n = (int) Math.floor(n / 10.0)) == 1) {
            ++n;
        }
        return WHAT.fromValue(n);
    }

    public static int parseDimmerLevel100(final LightingExt lighting) throws NumberFormatException {
        final String[] dimValues = lighting.getDimValues();
        if (lighting.getDim() != 1) {
            throw new NumberFormatException("Could not parse dimmerLevel100 from: " + lighting.getValue());
        }
        final int int1;
        if ((int1 = Integer.parseInt(dimValues[0])) >= 100 && int1 <= 200) {
            return int1 - 100;
        }
        throw new NumberFormatException("Value for dimmerLevel100 our of range. Msg = " + lighting.getValue());
    }

    @Override
    public OpenDeviceType detectDeviceType() {
        if (this.isCommand()) {
            final What what;
            OpenDeviceType openDeviceType;
            if ((what = this.getWhat()) == WHAT.OFF || what == WHAT.ON || what == WHAT.MOVEMENT_DETECTED
                    || what == WHAT.END_MOVEMENT_DETECTED) {
                openDeviceType = OpenDeviceType.SCS_ON_OFF_SWITCH;
            } else {
                openDeviceType = OpenDeviceType.SCS_DIMMER_SWITCH;
            }
            return openDeviceType;
        }
        return null;
    }

    public static LightingExt requestTurnOnWhatCustom(final String s, final int hour, final int minute,
            final int second, final Type type) {
        // Correzione provvisoria per la problematica nel group con doppio con .replaceFirst("\\##", "\\#") ##
        // BaseOpenMessage.a(s, type);
        return new LightingExt(String.format("*#%d*%s*#2*%s*%s*%s##", LightingExt.h,
                BaseOpenMessage.b(s, type).replaceFirst("\\##", "\\#"), hour, minute, second));
    }

    public static LightingExt requestTurnOnWhat(final String s, final int what, final Type type) {
        // Correzione provvisoria per la problematica nel group con doppio con .replaceFirst("\\##", "\\#") ##
        // BaseOpenMessage.a(s, type);
        return new LightingExt(String.format("*%d*%d*%s##", LightingExt.h, what,
                BaseOpenMessage.b(s, type).replaceFirst("\\##", "\\#")));
    }

    public static LightingExt requestMotionDetectorTurnOn(final String s, final Type type) {
        // Correzione provvisoria per la problematica nel group con doppio con .replaceFirst("\\##", "\\#") ##
        // BaseOpenMessage.a(s, type);
        return new LightingExt(String.format("*%d*%d*%s##", LightingExt.h, WHAT.MOVEMENT_DETECTED.value(),
                BaseOpenMessage.b(s, type).replaceFirst("\\##", "\\#")));
    }

    public static LightingExt requestMotionDetectorTurnOff(final String s, final Type type) {
        // Correzione provvisoria per la problematica nel group con doppio con .replaceFirst("\\##", "\\#") ##
        // BaseOpenMessage.a(s, type);
        return new LightingExt(String.format("*%d*%d*%s##", LightingExt.h, WHAT.END_MOVEMENT_DETECTED.value(),
                BaseOpenMessage.b(s, type).replaceFirst("\\##", "\\#")));
    }

    public static LightingExt requestMotionDetectorStatus(final String s, final Type type,
            final String RequestChannel) {
        // Correzione provvisoria per la problematica nel group con doppio con .replaceFirst("\\##", "\\#") ##
        // BaseOpenMessage.a(s, type);
        return new LightingExt(String.format("*#%d*%s*%s##", LightingExt.h,
                BaseOpenMessage.b(s, type).replaceFirst("\\##", "\\#"), RequestChannel));
    }

    static {
        g = LoggerFactory.getLogger(LightingExt.class);
        h = Who.LIGHTING.value();
    }

    public enum WHAT implements What {
        OFF(Integer.valueOf(0)),
        ON(Integer.valueOf(1)),
        DIMMER_20(Integer.valueOf(2)),
        DIMMER_30(Integer.valueOf(3)),
        DIMMER_40(Integer.valueOf(4)),
        DIMMER_50(Integer.valueOf(5)),
        DIMMER_60(Integer.valueOf(6)),
        DIMMER_70(Integer.valueOf(7)),
        DIMMER_80(Integer.valueOf(8)),
        DIMMER_90(Integer.valueOf(9)),
        DIMMER_100(Integer.valueOf(10)),
        TIMER_1_MIN(Integer.valueOf(11)),
        TIMER_2_MIN(Integer.valueOf(12)),
        TIMER_3_MIN(Integer.valueOf(13)),
        TIMER_4_MIN(Integer.valueOf(14)),
        TIMER_5_MIN(Integer.valueOf(15)),
        TIMER_15_MIN(Integer.valueOf(16)),
        TIMER_30_SEC(Integer.valueOf(17)),
        TIMER_05_SEC(Integer.valueOf(18)),
        BLINKING_05_SEC(Integer.valueOf(20)),
        BLINKING_1_SEC(Integer.valueOf(21)),
        BLINKING_1_5_SEC(Integer.valueOf(22)),
        BLINKING_2_SEC(Integer.valueOf(23)),
        BLINKING_2_5_SEC(Integer.valueOf(24)),
        BLINKING_3_SEC(Integer.valueOf(25)),
        BLINKING_3_5_SEC(Integer.valueOf(26)),
        BLINKING_4_SEC(Integer.valueOf(27)),
        BLINKING_4_5_SEC(Integer.valueOf(28)),
        BLINKING_5_SEC(Integer.valueOf(29)),
        DIMMER_UP(Integer.valueOf(30)),
        DIMMER_DOWN(Integer.valueOf(31)),
        DIMMER_TOGGLE(Integer.valueOf(32)),
        MOVEMENT_DETECTED(Integer.valueOf(34)),
        END_MOVEMENT_DETECTED(Integer.valueOf(39)),
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
