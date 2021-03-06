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

import bobby.main.KeyHandler;
import bobby.scene.enemies.Enemy;
import bobby.state.GameLevel;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * @author Kiarash Korki <kiarash96@users.sf.net>
 */
public class Player extends SceneObject {
	
	public int health;
	public int score;
	
	private static final double speed = 1.0;
	
	private int status;
	private int direction;
	private int blink;
	private static final int IDLE = 0;
	private static final int RUN = 1;
	
	// jump variables
	private int jumpStatus; // 0 = not jumping		1 = going up		2 = falling down
	private static final int maxJumpHeight = 160; 
	private double currentJumpHeight;
		
	// Animations
	private Sprite idleAnimation;
	private Sprite runAnimation;
	
	// jump images
	private Sprite jumpUpSprite;
	private Sprite jumpFallSprite;
	
	public static int WIDTH = 90;
	public static int HEIGHT = 135;
	
	public Player(SceneManager sm, double x) {
		super(sm, x, GameLevel.GROUND_LEVEL - HEIGHT, WIDTH, HEIGHT);
		
		health = 3;
		score = 0;
		
		status = IDLE;
		direction = +1;
		blink = 0;
		
		jumpStatus = 0;
		currentJumpHeight = 0;
		
		// load animations
		
		idleAnimation = new Sprite();
		idleAnimation.loadAnimatoion("rc/img/player", "idle", "png", 2);
		idleAnimation.setDelay(100);
		idleAnimation.scale(w, h);
		
		runAnimation = new Sprite();
		runAnimation.loadAnimatoion("rc/img/player", "run", "png", 6);
		runAnimation.setDelay(24);
		runAnimation.scale(w, h);
		
		// load jump images
		
		jumpUpSprite = new Sprite();
		jumpUpSprite.loadImage("rc/img/player/jump/jump-up.png");
		jumpUpSprite.scale(w, h);
		
		jumpFallSprite = new Sprite();
		jumpFallSprite.loadImage("rc/img/player/jump/jump-fall.png");
		jumpFallSprite.scale(w, h);
	}

	@Override
	public void update() {
		status = IDLE;
		if (KeyHandler.getKeyStatus(KeyEvent.VK_RIGHT) > 0) {
			x += speed;
			direction = +1;
			status = RUN;
		}
		if (KeyHandler.getKeyStatus(KeyEvent.VK_LEFT) > 0) {
			if (x - speed >= 0.0)
				x -= speed;
			direction = -1;
			status = RUN;
		}
		
		if (KeyHandler.getKeyStatus(KeyEvent.VK_SPACE) == KeyHandler.KEY_PRESS && jumpStatus == 0)
			jumpStatus = 1;
			
		// update jump
		if (jumpStatus == 1) {
			currentJumpHeight += speed;
			y -= speed;

			if (currentJumpHeight == maxJumpHeight)
				jumpStatus = 2;
		}
		else if (jumpStatus == 2) {
			currentJumpHeight -= speed;
			y += speed;

			if (currentJumpHeight == 0)
				jumpStatus = 0;
		}
		
		// intersect
		if (blink == 0) {
			CopyOnWriteArrayList<SceneObject> list = sm.getList();
			for (SceneObject object : list)
				if (object instanceof Enemy
					&& object.getBoundingBox().intersects(this.getBoundingBox())) {

					x = 10 + sm.offset;
					blink = 501;

					health --;
				}
		}
				
		
		// next frame in animation
		if (status == IDLE)
			idleAnimation.nextFrame();
		else if (status == RUN)
			runAnimation.nextFrame();
		
		// update blink
		blink = Math.max(blink - 1, 0);
	}

	@Override
	public void draw(Graphics g) {
		BufferedImage image;
		if (status == RUN)
			image = runAnimation.getCurrentImage();
		else // idle
			image = idleAnimation.getCurrentImage();
		
		if (jumpStatus == 1)
			image = jumpUpSprite.getCurrentImage();
		else if (jumpStatus == 2)
			image = jumpFallSprite.getCurrentImage();
		
		if (blink % 50 < 25)
			super.drawWithOffset(g, image, x, y, w, h, direction);
	}
	
}
