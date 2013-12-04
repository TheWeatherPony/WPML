package weatherpony.launch;

import java.util.Collection;
import java.util.List;

public interface WPMLmod {
	String modName();
	String currentVersion();
	
	void giveSelf(WPMModContainer self);
	void informOfWPMods(Collection<WPMModContainer> wpModList);
	/**
	 * @param comparedTo a version of this mod. It may be past, present, or future.
	 * @return a negative value if a past version, zero if current version or empty string, or a positive number if a future version
	 */
	int comparedToOtherVersion(String comparedTo);
	
	void preSecondaryInjection();
	void postSecondaryInjection();
	List getWPMMergeRequests(String normalName, String transformedName);
	void closeOpenWPMMergeRequests(String normalName, String transformedName);
	List<IClassManipulator> postWPMLMergers(String normalName, String transformedName);
	
	List<IClassManipulator> postWPMLMergers();
}
