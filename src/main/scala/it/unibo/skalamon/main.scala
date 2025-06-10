package it.unibo.skalamon

import asciiPanel.AsciiFont
import asciiPanel.AsciiPanel

import javax.swing.*
import java.awt.*
import java.util.Random

@main
def main(): Unit =
  var TITLE: String = "Demo AsciiPanel";
  // Initialize the frame, terminal, and random number generator
  // JFrame is the container for the terminal
  var frame: JFrame =
    new JFrame(TITLE); // Create a new JFrame with the title "Demo AsciiPanel"
  var terminal: AsciiPanel =
    new AsciiPanel(); // Create a new AsciiPanel instance (the terminal)
  var rand: Random =
    new Random(); // Initialize the random generator for background color selection

  // Write the title in the center of the terminal at row 2
  terminal.writeCenter(TITLE, 2);

  // Write the text "Red color" in red at row 5
  terminal.writeCenter("Red color", 5, AsciiPanel.red);

  // Random generation loop: write spaces with random background colors
  // Iterate through a specific region (from x = 15 to 45, y = 10 to 20)
  for (x <- 15 until 45) {
    for (y <- 10 until 20) {
      if (rand.nextInt(2) == 0) {
        terminal.setDefaultBackgroundColor(AsciiPanel.blue)
      } else {
        terminal.setDefaultBackgroundColor(AsciiPanel.green)
      }

      terminal.write(" ", x, y)
    }
  }

  // Add the terminal to the frame
  // Set the frame properties: make it non-resizable, pack it to fit the terminal, and set default close operation
  frame.add(terminal);
  frame.setResizable(false); // Disable resizing of the window
  frame.pack(); // Adjust the window size to fit the terminal size
  frame.setDefaultCloseOperation(
    WindowConstants.EXIT_ON_CLOSE
  ); // Ensure the application exits when the window is closed
  frame.setVisible(true); // Make the window visible to the user
