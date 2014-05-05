/*
 * This is free and unencumbered software released into the public domain.
 * 
 * Anyone is free to copy, modify, publish, use, compile, sell, or distribute this software, either
 * in source code form or as a compiled binary, for any purpose, commercial or non-commercial, and
 * by any means.
 * 
 * In jurisdictions that recognize copyright laws, the author or authors of this software dedicate
 * any and all copyright interest in the software to the public domain. We make this dedication for
 * the benefit of the public at large and to the detriment of our heirs and successors. We intend
 * this dedication to be an overt act of relinquishment in perpetuity of all present and future
 * rights to this software under copyright law.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * 
 * For more information, please refer to <http://unlicense.org/>
 */

package com.github.pffy.hanzitopinyin;

import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.net.URI;

import javax.swing.JOptionPane;

import com.github.pffy.chinese.HanyuPinyin;
import com.github.pffy.chinese.Tone;

/**
 * HanziToPinyin.java - Hanzi-to-Pinyin Converter. Requires Java 7.
 * 
 * @author The Pffy Authors
 * @version 0.9.2
 * 
 */

@SuppressWarnings("serial")
public class HanziToPinyin extends javax.swing.JFrame {

  // Constants
  private final String CRLF = "\r\n";
  private final String PRODUCT_NAME = "InPinyin Hanzi-to-Pinyin Converter";
  private final String VERSION = CRLF + "Version 0.9.2";
  private final String AUTHORS = CRLF + CRLF + "The Pffy Authors" + CRLF
      + "https://github.com/pffy";
  private final String LICENSE = CRLF + CRLF + "This is free, libre and "
      + "open source software. " + CRLF + "http://unlicense.org/";
  private final String PROJECT_URL = "https://github.com/pffy/" + "java-swing-hanzitopinyin";
  private final String ISSUES_URL = PROJECT_URL + "/issues";

  private final String ITEM_NEW = "New";
  private final String ITEM_CONVERT = "Convert";
  private final String ITEM_EXIT = "Exit";

  private final String ITEM_TONE_NUMBERS = "Convert to Tone Numbers";
  private final String ITEM_TONE_MARKS = "Convert to Tone Marks";
  private final String ITEM_TONES_OFF = "Convert with Tones Off";
  private final String ITEM_AUTOCONVERT = "Auto-Convert";

  private final String ITEM_REPORT = "Report Issue...";
  private final String ITEM_HOMEPAGE = "Project Home Page";
  private final String ITEM_ABOUT = "About InPinyin";

  private final Dimension MIN_APP_SIZE = new Dimension(350, 350);
  private final Dimension PREFERRED_APP_SIZE = new Dimension(600, 600);

  // menu
  private javax.swing.JMenuBar menubar_menu;

  // file menu
  private javax.swing.JMenu menu_file;
  private javax.swing.JMenuItem file_new;
  private javax.swing.JMenuItem file_convert;
  private javax.swing.JPopupMenu.Separator file_separator;
  private javax.swing.JMenuItem file_exit;

  // help menu
  private javax.swing.JMenu menu_help;
  private javax.swing.JMenuItem help_report;
  private javax.swing.JPopupMenu.Separator help_separator;
  private javax.swing.JMenuItem help_homepage;
  private javax.swing.JMenuItem help_about;

  // options
  private javax.swing.JMenu menu_options;
  private javax.swing.JCheckBoxMenuItem options_tone_numbers;
  private javax.swing.JCheckBoxMenuItem options_tone_marks;
  private javax.swing.JCheckBoxMenuItem options_tones_off;
  private javax.swing.JPopupMenu.Separator options_separator;
  private javax.swing.JCheckBoxMenuItem options_autoconvert;

  // inputs and outputs
  private javax.swing.JScrollPane spane_input;
  private javax.swing.JScrollPane spane_output;
  private javax.swing.JTextPane tpane_input;
  private javax.swing.JTextPane tpane_output;

  // HanyuPinyin object
  HanyuPinyin hp = new HanyuPinyin();


  /**
   * Builds this object.
   */
  public HanziToPinyin() {
    initComponents();
    updateMenusByOptions();
    updateToneOptions();
  };


  // setup components
  private void initComponents() {

    // menu
    menubar_menu = new javax.swing.JMenuBar();

    // file menu
    menu_file = new javax.swing.JMenu();
    menu_file.setMnemonic('f');
    menu_file.setText("File");

    file_new = new javax.swing.JMenuItem();
    file_convert = new javax.swing.JMenuItem();
    file_separator = new javax.swing.JPopupMenu.Separator();
    file_exit = new javax.swing.JMenuItem();


    // options menu
    menu_options = new javax.swing.JMenu();
    menu_options.setMnemonic('o');
    menu_options.setText("Options");

    options_tone_numbers = new javax.swing.JCheckBoxMenuItem();
    options_tone_marks = new javax.swing.JCheckBoxMenuItem();
    options_tones_off = new javax.swing.JCheckBoxMenuItem();
    options_separator = new javax.swing.JPopupMenu.Separator();
    options_autoconvert = new javax.swing.JCheckBoxMenuItem();

    // help menu
    menu_help = new javax.swing.JMenu();
    menu_help.setMnemonic('H');
    menu_help.setText("Help");

    help_report = new javax.swing.JMenuItem();
    help_separator = new javax.swing.JPopupMenu.Separator();
    help_homepage = new javax.swing.JMenuItem();
    help_about = new javax.swing.JMenuItem();

    // inputs and outputs
    spane_input = new javax.swing.JScrollPane();
    tpane_input = new javax.swing.JTextPane();
    spane_output = new javax.swing.JScrollPane();
    tpane_output = new javax.swing.JTextPane();

    // frame properties
    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    setTitle(PRODUCT_NAME);

    // frame sizing
    setMinimumSize(MIN_APP_SIZE);
    setPreferredSize(PREFERRED_APP_SIZE);

    // BoxLayout for left-right panes
    getContentPane().setLayout(
        new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.LINE_AXIS));

    tpane_input.addKeyListener(keyHandler);

    spane_input.setViewportView(tpane_input);
    getContentPane().add(spane_input);

    tpane_output.setEditable(false);
    spane_output.setViewportView(tpane_output);

    getContentPane().add(spane_output);

    // file
    file_new.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N,
        java.awt.event.InputEvent.CTRL_MASK));
    file_new.setMnemonic('N');
    file_new.setText(ITEM_NEW);
    file_new.addActionListener(menuHandler);

    menu_file.add(file_new);

    file_convert.setAccelerator(javax.swing.KeyStroke
        .getKeyStroke(java.awt.event.KeyEvent.VK_F5, 0));
    file_convert.setMnemonic('C');
    file_convert.setText(ITEM_CONVERT);
    file_convert.addActionListener(menuHandler);

    menu_file.add(file_convert);
    menu_file.add(file_separator);

    file_exit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_W,
        java.awt.event.InputEvent.CTRL_MASK));
    file_exit.setMnemonic('x');
    file_exit.setText(ITEM_EXIT);
    file_exit.addActionListener(menuHandler);

    menu_file.add(file_exit);
    menubar_menu.add(menu_file);

    // OPTIONS

    // options > tone numbers
    options_tone_numbers.setAccelerator(javax.swing.KeyStroke.getKeyStroke(
        java.awt.event.KeyEvent.VK_F7, 0));
    options_tone_numbers.setText(ITEM_TONE_NUMBERS);
    options_tone_numbers.addActionListener(menuHandler);

    menu_options.add(options_tone_numbers);

    // options > tone marks
    options_tone_marks.setAccelerator(javax.swing.KeyStroke.getKeyStroke(
        java.awt.event.KeyEvent.VK_F8, 0));
    options_tone_marks.setText(ITEM_TONE_MARKS);
    options_tone_marks.addActionListener(menuHandler);

    menu_options.add(options_tone_marks);

    // options > tones off
    options_tones_off.setAccelerator(javax.swing.KeyStroke.getKeyStroke(
        java.awt.event.KeyEvent.VK_F9, 0));
    options_tones_off.setText(ITEM_TONES_OFF);
    options_tones_off.addActionListener(menuHandler);

    menu_options.add(options_tones_off);
    menu_options.add(options_separator);

    // options > auto-convert
    options_autoconvert.setAccelerator(javax.swing.KeyStroke.getKeyStroke(
        java.awt.event.KeyEvent.VK_F10, 0));
    options_autoconvert.setSelected(true);
    options_autoconvert.setText(ITEM_AUTOCONVERT);
    options_autoconvert.addActionListener(menuHandler);

    menu_options.add(options_autoconvert);
    menubar_menu.add(menu_options);

    // HELP

    // help > report issue
    help_report
        .setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F2, 0));
    help_report.setMnemonic('i');
    help_report.setText(ITEM_REPORT);
    help_report.addActionListener(menuHandler);

    menu_help.add(help_report);
    menu_help.add(help_separator);

    // help > home page
    help_homepage.setMnemonic('h');
    help_homepage.setText(ITEM_HOMEPAGE);
    help_homepage.addActionListener(menuHandler);

    menu_help.add(help_homepage);

    // help > about
    help_about.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, 0));
    help_about.setMnemonic('a');
    help_about.setText(ITEM_ABOUT);
    help_about.addActionListener(menuHandler);

    menu_help.add(help_about);
    menubar_menu.add(menu_help);

    setJMenuBar(menubar_menu);
    pack();
  };

  /**
   * Event Listeners
   * 
   */

  // handles key strokes
  private KeyListener keyHandler = new KeyListener() {

    public void keyTyped(KeyEvent e) {
      // leave empty
    }

    public void keyPressed(KeyEvent e) {
      // leave empty
    }

    public void keyReleased(KeyEvent e) {
      autoConvertInput();
    }
  };


  // handles JMenu actions
  private ActionListener menuHandler = new ActionListener() {

    public void actionPerformed(ActionEvent e) {

      // FIXME: String cases with menu items? 
      // Hmm. Not sure about this.

      switch (e.getActionCommand()) {
        
      // file cases
        case ITEM_NEW:
          // clear everything
          clearAllText();
          break;
        case ITEM_CONVERT:
          // converts input on demand
          convertInput();
          break;
        case ITEM_EXIT:
          // closes program
          System.exit(0);
          break;

        // options cases
        case ITEM_TONE_NUMBERS:
          resetAllToneOptions();
          options_tone_numbers.setSelected(true);
          hp.setMode(Tone.TONE_NUMBERS);
          autoConvertInput();
          break;
        case ITEM_TONE_MARKS:
          resetAllToneOptions();
          options_tone_marks.setSelected(true);
          hp.setMode(Tone.TONE_MARKS);
          autoConvertInput();
          break;
        case ITEM_TONES_OFF:
          resetAllToneOptions();
          options_tones_off.setSelected(true);
          hp.setMode(Tone.TONES_OFF);
          autoConvertInput();
          break;
        case ITEM_AUTOCONVERT:
          updateMenusByOptions();
          break;

        // help cases
        case ITEM_REPORT:
          // sends user to issues page
          openWebpage(URI.create(ISSUES_URL));
          break;
        case ITEM_HOMEPAGE:
          // sends user to home page
          openWebpage(URI.create(PROJECT_URL));
          break;
        case ITEM_ABOUT:
          showAboutBox();
          break;

        default:
          // nothing
          break;
      }

    };
  };


  // clears both input and output
  private void clearAllText() {
    tpane_input.setText("");
    tpane_output.setText("");
  };

  // shows about box
  private void showAboutBox() {
    JOptionPane.showMessageDialog(null, PRODUCT_NAME + VERSION + AUTHORS + LICENSE,
        "About InPinyin", JOptionPane.PLAIN_MESSAGE);
  };


  // opens web page in default browser
  private void openWebpage(URI uri) {

    // code solution found at:
    // http://stackoverflow.com/a/10967469

    Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
    if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
      try {
        desktop.browse(uri);
      } catch (IOException ioe) {
        // may fail due to permissions restrictions
        // it is okay to fail quietly
      }
    }
  };


  // converts if autoConvert option is selected
  private void autoConvertInput() {
    if (options_autoconvert.isSelected()) {
      convertInput();
    }
  };


  // updates menus based on options
  private void updateMenusByOptions() {
    if (options_autoconvert.isSelected()) {
      file_convert.setEnabled(false);
    } else {
      file_convert.setEnabled(true);
    }
    autoConvertInput();
  };


  // updates tone options based on HanyuPinyin object
  private void updateToneOptions() {

    resetAllToneOptions();
    switch (hp.getMode()) {
      case TONE_MARKS:
        options_tone_marks.setSelected(true);
        break;
      case TONES_OFF:
        options_tones_off.setSelected(true);
        break;
      default:
        options_tone_numbers.setSelected(true);
        break;
    }

    autoConvertInput();
  };


  // convert input and display output
  private void convertInput() {
    hp.setInput(tpane_input.getText());
    tpane_output.setText(hp.toString());
  };


  // clear all tone options
  private void resetAllToneOptions() {
    options_tone_numbers.setSelected(false);
    options_tone_marks.setSelected(false);
    options_tones_off.setSelected(false);
  };


  /**
   * Main method. Creates and shows the form.
   * @param args 
   */
  public static void main(String args[]) {

    // create form, make form visible
    java.awt.EventQueue.invokeLater(new Runnable() {
      public void run() {
        new HanziToPinyin().setVisible(true);
      }
    });
  };

};
