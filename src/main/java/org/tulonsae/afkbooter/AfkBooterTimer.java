package org.tulonsae.afkbooter;

import java.util.logging.Logger;

import net.escapecraft.escapePlug.EscapePlug;

/**
 * AfkBooter component thread that checks for idle players to kick.
 *
 * @author Tulonsae
 * Original author neromir.
 */
public class AfkBooterTimer extends Thread
{
    private EscapePlug plugin;
    private AfkBooter afkBooter;
    private Logger log;
    private long timeToSleep;
    private boolean aborted;

    public AfkBooterTimer(AfkBooter afkBooter, long timeToSleep)
    {
        this.afkBooter = afkBooter;
        this.plugin = afkBooter.getPlugin();
        this.timeToSleep = timeToSleep;
        this.aborted = false;
        this.log = plugin.getLogger();

        if (afkBooter.getDebugFlag()) log.info("AfkBooter: Debug: created AfkBooterTimer object.");
    }

    @Override
    public void run()
    {
        while(!aborted)
        {
            try
            {
                afkBooter.kickAfkPlayers();
                Thread.sleep(timeToSleep);
            }
            catch(InterruptedException e)
            {
                plugin.getLogger().severe("AfkBooterTimer thread interrupted while sleeping.");
                e.printStackTrace();
            }
        }
    }

    public void setTimeToSleep(long timeToSleep)
    {
        this.timeToSleep = timeToSleep;
    }

    public void setAborted(boolean aborted)
    {
        this.aborted = aborted;
    }
}
