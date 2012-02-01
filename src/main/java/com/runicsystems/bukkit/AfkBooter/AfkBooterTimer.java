package com.runicsystems.bukkit.AfkBooter;

import java.util.logging.Level;

import net.escapecraft.escapePlug.EscapePlug;

/**
 * @author neromir
 */
public class AfkBooterTimer extends Thread
{
    private EscapePlug plugin;
    private AfkBooter afkBooter;
    private long timeToSleep;
    private boolean aborted;

    public AfkBooterTimer(AfkBooter afkBooter, long timeToSleep)
    {
        this.afkBooter = afkBooter;
        this.plugin = afkBooter.getPlugin();
        this.timeToSleep = timeToSleep;
        this.aborted = false;
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
