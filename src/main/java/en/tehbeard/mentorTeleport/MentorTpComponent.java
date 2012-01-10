package en.tehbeard.mentorTeleport;

import net.escapecraft.escapePlug.EscapePlug;
import net.escapecraft.escapePlug.component.AbstractComponent;
import net.escapecraft.escapePlug.component.ComponentDescriptor;

@ComponentDescriptor(name = "Mentor TP", slug = "mentortp", version = "1.1")
public class MentorTpComponent extends AbstractComponent{

	@Override
	public boolean enable(EscapePlug plugin) {
		
		plugin.registerCommands(new MentorTeleport(plugin));
		plugin.registerCommands(new MentorBack(plugin));
		
		return true;
	}

	@Override
	public void tidyUp() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reloadConfig() {
		// TODO Auto-generated method stub
		
	}

}
