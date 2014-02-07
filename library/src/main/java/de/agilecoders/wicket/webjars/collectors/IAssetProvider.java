package de.agilecoders.wicket.webjars.collectors;

import java.util.Set;
import java.util.SortedMap;

/**
 * base provider interface
 *
 * @author miha
 */
public interface IAssetProvider {

    /**
     * List assets within a folder.
     *
     * @param folderPath the root path to the folder. Must begin with '/'.
     * @return a set of folder paths that match.
     */
    Set<String> listAssets(final String folderPath);

    /**
     * @return the full path index map.
     */
    SortedMap<String, String> getFullPathIndex();
}
