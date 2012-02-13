package com.weborganic.smith.rule;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.weborganic.smith.PasswordRule;
import com.weborganic.smith.Scriptable;

/**
 * Evaluate a password by checking against a list of banned passwords.
 *
 * @author Christophe Lauret
 * @version 14 February 2012
 */
public class BannedPasswordRule implements PasswordRule, Scriptable {

  /**
   * The set of banned passwords.
   */
  private Set<String> _banned = null;

  /**
   * The score when the password is banned.
   */
  private int forBanned = -100;

  /**
   * The score when the password is allowed.
   */
  private int forAllowed = 0;

  @Override
  public int score(String password) {
    if (this._banned == null) this._banned = load();
    if (this._banned.contains(password)) {
      return this.forBanned;
    } else {
      return this.forAllowed;
    }
  }

  @Override
  public void configure(Map<String, String> config) {
    Set<String> banned = null;
    // Retrieve list from file
    String file = config.get("file");
    if (file != null && !"#default".equals(file)) {
      try {
        banned = load(new FileReader(file));
      } catch (IOException ex) {
        throw new IllegalArgumentException(ex);
      }
    }
    // Retrieve a list if one is specified
    String list = config.get("list");
    if (list != null) {
      String[] words = list.split(",");
      if (banned == null) banned = new HashSet<String>();
      for (String word : words) {
        banned.add(word);
      }
    }
    this._banned = banned;
  }

  // Static helpers
  // ------------------------------------------------------------------------------------------------------------------

  /**
   * Loads the default banned passwords.
   */
  public static Set<String> load() {
    ClassLoader loader = BannedPasswordRule.class.getClassLoader();
    InputStream in = null;
    Set<String> banned = new HashSet<String>();
    try {
      in = loader.getResourceAsStream("com/weborganic/smith/rule/banned.txt");
      InputStreamReader r = new InputStreamReader(in);
      banned = load(r);
    } catch (IOException ex) {
      throw new IllegalStateException(ex);
    } finally {
      try {
        if (in != null) in.close();
      } catch (IOException ex) {
        ex.printStackTrace();
      }
    }
    return banned;
  }

  /**
   * Loads the passwords found in the specified reader.
   *
   * @param reader the reader on a list of banned passwords.
   */
  public static Set<String> load(Reader reader) throws IOException {
    BufferedReader buffered = null;
    Set<String> banned = new HashSet<String>();
    try {
      buffered = new BufferedReader(reader);
      String word = buffered.readLine();
      while (word != null) {
        banned.add(word);
        word = buffered.readLine();
      }
    } catch (IOException ex) {
      throw new IllegalStateException(ex);
    } finally {
      try {
        if (buffered != null) buffered.close();
      } catch (IOException ex) {
        ex.printStackTrace();
      }
    }
    return banned;
  }

  @Override
  public Appendable toScript(Appendable script) throws IOException {
    script.append("function (p) {");
    // Store array
    script.append(" var b = [");
    for (Iterator<String> words = this._banned.iterator(); words.hasNext(); ) {
      String word = words.next();
      // TODO escape if contains single quote
      script.append("'"+word+"'");
      if (words.hasNext()) script.append(',');
    }
    script.append("];");
    script.append(" return b.indexOf(p) !== -1? "+this.forBanned+" : "+this.forAllowed+";");
    script.append("}");
    return script;
  }
}
