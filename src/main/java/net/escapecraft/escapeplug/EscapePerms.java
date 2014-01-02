package net.escapecraft.escapeplug;

public class EscapePerms {

    public static final String ALL_KITS = "escapeplug.kit.kits.*";
    public static final String ALL_KITS_NO_WAIT = "escapeplug.kit.kits.*.nocooldown";
    // edit warps lets you set and remove them
    public static final String EDIT_WARPS = "escapeplug.warps.edit";
    public static final String GET_BLOCK_ALERTS_SPAM = "escapeplug.blockalert.notify";
    public static final String HAS_MENTORTP = "escapeplug.mentor.teleport";
    public static final String HAS_MENTORTP_IMMUNITY = "escapeplug.mentor.teleport.notarget";
    public static final String HAS_RESERVE = "escapeplug.reserve.allow";
    public static final String HAS_TOURBUS_PORT = "escapeplug.tourbus.port";
    public static final String IGNORE_HORSEMOD_LIMITS = "escapeplug.horsemod.override.*";
    public static final String LIST_WARPS = "escapeplug.warps";
    public static final String MANAGE_WARPS = "escapeplug.warps.*";
    public static final String MAY_DAMAGE_HORSE = "escapeplug.horsemod.override.damage";
    public static final String MAY_ACCESS_ANY_HORSE_INV = "escapeplug.horsemod.override.inventory";
    public static final String MAY_RIDE_ANY_HORSE = "escapeplug.horsemod.override.ride";
    public static final String MAY_XFER_ANY_HORSE = "escapeplug.horsemod.override.transfer";
    // the named kit expects the code to add the kit name
    public static final String NAMED_KIT = "escapeplug.kit.kits.";
    // the no wait named kit expect the code to replace NAME with the kit name
    public static final String NAMED_KIT_NO_WAIT = "escapeplug.kit.kits.NAME.nocooldown";
    public static final String SWITCH_GAMEMODE = "escapeplug.gamemode";
    public static final String USE_AREABLOCK = "escapeplug.areablock";
    public static final String USE_PIGJOUST = "escapeplug.pigjoust";
    public static final String USE_WARPS = "escapeplug.warps.tele";

    // TODO finish BlockAlert and the other components
}
