/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nl.b3p.datastorelinker.util;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import net.sourceforge.stripes.util.Log;

/**
 *
 * @author Erik van de Pol
 */
public class Util {
    private final static Log log = Log.getInstance(Util.class);

    public static void addToMapIfNotNull(Map<String, Object> map, String key, Object value) {
        addToMapIfNotNull(map, key, value, "");
    }

    public static void addToMapIfNotNull(Map<String, Object> map, String key, Object value, String keyPrefix) {
        if (keyPrefix == null)
            keyPrefix = "";
        if (key != null && value != null)
            map.put(keyPrefix + key, value);
    }

    public static Map<String, Object> fileToMap(File file) {
        return fileToMap(file, "");
    }

    public static Map<String, Object> fileToMap(File file, String keyPrefix) {
        if (file.isDirectory()) {
            // TODO: throw ex?
            log.error("Attempt to run dsl with a directory as input");
        }

        Map<String, Object> map = new HashMap<String, Object>();

        Object qname = null;
        if (file.exists()) {
            //qname = file.toURI().getPath();
            try {
                // heel belangrijk voor de DatastoreLinker / Geotools!!
                qname = file.toURI().toURL();
            } catch(Exception e) {
                log.error("Malformed file url: " + e.getMessage());
            }
        } else {
            log.error("File does not exist: " + file);
        }

        Util.addToMapIfNotNull(map, "url", qname, keyPrefix);
        Util.addToMapIfNotNull(map, "srs", "EPSG:28992", keyPrefix);

        return map;
    }

}
