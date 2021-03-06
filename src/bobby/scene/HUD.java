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
package bobby.scene;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

/**
 *
 * @author Kiarash Korki <kiarash96@users.sf.net>
 */
public class HUD {

	private SceneManager sm; // reference to sm
	
	Sprite healthAnim;
	Sprite scoreAnim;
	
	public HUD(SceneManager sm) {
		this.sm = sm;
		
		healthAnim = new Sprite();
		healthAnim.loadAnimatoion("rc/img/items/", "health", "png", 8);
		healthAnim.setDelay(15);
		healthAnim.setBase(0, 600);
		healthAnim.scale(50, 45);
		
		scoreAnim = new Sprite();
		scoreAnim.loadAnimatoion("rc/img/items/", "coin", "png", 10);
		scoreAnim.setDelay(15);
		scoreAnim.setBase(0, 600);
		scoreAnim.scale(50, 50);
	}

	public void update() {
		healthAnim.nextFrame();
		scoreAnim.nextFrame();
	}
	
	public void draw(Graphics g) {
		for (int i = 0; i < sm.getPlayer().health; i ++)
			g.drawImage(healthAnim.getCurrentImage(), 10 + 60 * i, 10, null);
		
		g.drawImage(scoreAnim.getCurrentImage(), 10, 70, null);
		g.setFont(new Font("TimesRoman", Font.PLAIN, 45)); 
		g.setColor(Color.BLACK);
		g.drawString(Integer.toString(sm.getPlayer().score), 70, 110);
	}
	
}
