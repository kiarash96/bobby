/*
 * The MIT License
 *
 * Copyright 2015 Kiarash Korki <kiarash96@users.sf.net>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package bobby.state;

import bobby.main.KeyHandler;
import java.awt.Graphics;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Kiarash Korki <kiarash96@users.sf.net>
 */
public class GameStateManager implements Runnable {

	public static final int sleepTime = 5;
	
	GameLevel level;
	
	public GameStateManager() {
		level = new GameLevel();
	}
	
	public void update() {
		level.update();
	}

	public void draw(Graphics g) {
		level.draw(g);
	}

	@Override
	public void run() {
		while (true) {
			KeyHandler.update();
			this.update();
			
			try {
				Thread.currentThread().sleep(sleepTime);
			}
			catch (InterruptedException ex) {
				Logger.getLogger(GameStateManager.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}
	
}