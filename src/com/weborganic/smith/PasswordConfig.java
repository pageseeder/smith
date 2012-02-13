package com.weborganic.smith;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * A password configuration.
 *
 * @author Christophe Lauret
 * @version 9 February 2012
 */
public class PasswordConfig {

  /**
   * The default configuration
   */
  private static PasswordConfig defaultConfig = null;

  /**
   * The thresholds (minimum value that the score must match) for levels at the same index.
   */
  private final int[] _thresholds;

  /**
   * The level corresponding to the threshold at the same index.
   */
  private final String[] _levels;

  /**
   * The rules to apply.
   */
  private List<PasswordRule> _rules;

  /**
   * Creates a new password configuration.
   */
  protected PasswordConfig(SortedMap<Integer, String> levels, List<PasswordRule> rules) {
    this._thresholds = new int[levels.size()];
    this._levels = new String[levels.size()];
    int i = 0;
    for (Entry<Integer, String> e : levels.entrySet()) {
      this._thresholds[i] = e.getKey().intValue();
      this._levels[i] = e.getValue();
      i++;
    }
    this._rules = rules;
  }

  /**
   * Returns the level for a given score.
   *
   * @param score the score
   * @return the corresponding level.
   */
  public String getLevel(int score) {
    int i = -1;
    for (int threshold : this._thresholds) {
      if (score < threshold) break;
      i++;
    }
    return this._levels[i];
  }

  /**
   * Returns the rules in this configuration
   *
   * @return the rules in this configuration.
   */
  public List<PasswordRule> rules() {
    return this._rules;
  }

  // Static helpers
  // ------------------------------------------------------------------------------------------------------------------

  /**
   * Returns the default configuration
   */
  public static PasswordConfig defaultConfig() {
    if (defaultConfig == null) {

        ClassLoader loader = PasswordConfig.class.getClassLoader();
        try {
          InputStream in = loader.getResourceAsStream("com/weborganic/smith/config.xml");
          InputSource source = new InputSource(in);
          defaultConfig = load(source);
        } catch (Exception ex) {
          ex.printStackTrace();
        }

    }
    return defaultConfig;
  }

  /**
   * Parses the specified file.
   *
   * @return the corresponding configuration.
   */
  public static PasswordConfig load(File file) {
    FileInputStream in = null;
    PasswordConfig config = null;
    try  {
      in = new FileInputStream(file);
      InputSource source = new InputSource(in);
      config = PasswordConfig.load(source);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return config;
  }

  /**
   * Parses the specified file.
   *
   * @return the corresponding configuration.
   */
  public static PasswordConfig load(InputSource source) {
    try {
      SAXParserFactory factory = SAXParserFactory.newInstance();
      SAXParser parser = factory.newSAXParser();
      ConfigHandler handler = new ConfigHandler();
      parser.parse(source, handler);
      return new PasswordConfig(handler.levels, handler.rules);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    // TODO
    return null;
  }

  /**
   * Handler for the config.
   *
   * @author Christophe Lauret
   * @version 9 February 2012
   */
  private static class ConfigHandler extends DefaultHandler {

    /**
     * The levels.
     */
    private SortedMap<Integer, String> levels = new TreeMap<Integer, String>();

    /**
     * The rules to apply.
     */
    private List<PasswordRule> rules = new ArrayList<PasswordRule>();

    /**
     * The properties for the current rule.
     */
    private Map<String, String> properties = null;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
      String element = qName;
      if ("levels".equals(element)) {
        // Default threshold
        this.levels.put(Integer.MIN_VALUE, atts.getValue("default"));
      } else if ("level".equals(element)) {
        // Predefined threshold
        Integer threshold = Integer.valueOf(atts.getValue("threshold"));
        this.levels.put(threshold, atts.getValue("name"));
      } else if ("rule".equals(element)) {
        String className = atts.getValue("class");
        if (className.indexOf('.') < 0) {
          className = "com.weborganic.smith.rule."+className;
        }
        try {
          Class<?> c = Class.forName(className);
          Object o = c.newInstance();
          if (o instanceof PasswordRule) {
            PasswordRule rule = (PasswordRule)o;
            this.rules.add(rule);
          }
        } catch (Exception ex) {
          throw new SAXException(ex);
        }
      } else if ("property".equals(element)) {
        String name = atts.getValue("name");
        String value = atts.getValue("value");
        if (this.properties == null) this.properties = new HashMap<String, String>();
        this.properties.put(name, value);
      }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
      String element = qName;
      if ("rule".equals(element)) {
        PasswordRule rule = this.rules.get(this.rules.size()-1);
        rule.configure(this.properties);
        this.properties = null;
      }
    }
  }

}