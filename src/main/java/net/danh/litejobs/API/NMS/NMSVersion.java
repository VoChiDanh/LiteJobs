package net.danh.litejobs.API.NMS;

import org.bukkit.Bukkit;

import java.util.Objects;

/**
 * @author Callum Johnson
 * @version 1.0
 * @since 11/12/2021 - 22:47
 */
public class NMSVersion {


    /**
     * The major version of Minecraft.
     * <p>
     * Usually, and probably always going to be... '1'.
     * </p>
     */
    private final int major;

    /**
     * The minor version of Minecraft.
     * <p>
     * The minor version, for example '8' or '7' or most-recently '18'.
     * </p>
     */
    private final int minor;

    /**
     * The revision, 1.7.10 = 'R4' etc. etc.
     */
    private final int revision;

    /**
     * Constructor to initialise the NMSVersion data-type.
     * <p>
     * Initialises the {@link #major}, {@link #minor} and {@link #revision} variables,
     * for usage with the {@link NMSAssistant} or by Ponder Developers.
     * </p>
     */
    public NMSVersion() {
        String version = Bukkit.getServer().getClass().getPackage().getName();
        version = version.substring(version.lastIndexOf(".") + 1).replace("v", "").trim();
        final String[] versionDetails = version.split("_");
        major = Integer.parseInt(versionDetails[0]); // Always probably going to be '1'.
        minor = Integer.parseInt(versionDetails[1]); // 16/18/7/8 etc. etc.
        revision = Integer.parseInt(versionDetails[2].replace("R", "").trim());
    }

    public int getMajor() {
        return major;
    }

    public int getMinor() {
        return minor;
    }

    public int getRevision() {
        return revision;
    }

    @Override
    public String toString() {
        return "v" + getMajor() + "_" + getMinor() + "_R" + getRevision();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        NMSVersion that = (NMSVersion) o;
        return major == that.major && minor == that.minor && revision == that.revision;
    }

    @Override
    public int hashCode() {
        return Objects.hash(major, minor, revision);
    }
}
