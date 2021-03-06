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

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author Kiarash Korki <kiarash96@users.sf.net>
 */
public class Sprite implements Serializable {
	
	transient private BufferedImage[] frames;
	
	// for loading images in deserialization
	
	int w, h;
	
	private static int TYPE_IMAGE = 1;
	private static int TYPE_ANIMATION = 2;
	private int type;
	
	String filepath;
	
	String dir, name, ext;
	int frameCount;
	
	// frame and delay details
	
	private int currentFrame;
	private int delay, currentDelay;
	private int baseFrame, baseDelay;
	
	public Sprite() {
		currentFrame = 0;
		delay = 1; currentDelay = 0;
		baseFrame = -1; baseDelay = 1;
	}
	
	public void loadImage(String filepath) {
		this.type = TYPE_IMAGE;
		this.filepath = filepath;
		this.frameCount = 1;
		
		frames = new BufferedImage[1];
		try {
			frames[0] = ImageIO.read(new File(filepath));
		}
		catch (IOException ex) {
			Logger.getLogger(Sprite.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	public void loadAnimatoion(String dir, String name, String ext, int frameCount) {
		this.type = TYPE_ANIMATION;
		this.dir = dir;
		this.name = name;
		this.ext = ext;
		this.frameCount = frameCount;
		
		frames = new BufferedImage[frameCount];
		for (int i = 0; i < frameCount; i ++)
			try {
				frames[i] = ImageIO.read(new File(dir + "/" + name + "/frame-" + (i+1) + "." + ext));
			}
			catch (IOException ex) {
				Logger.getLogger(Sprite.class.getName()).log(Level.SEVERE, null, ex);
			}
	}
	
	public void scale(int width, int height) {
		for (int i = 0; i < frames.length; i ++) {
			Image tmp = frames[i].getScaledInstance(width, height, Image.SCALE_SMOOTH);
			frames[i] = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			frames[i].createGraphics().drawImage(tmp, 0, 0, null);
			frames[i].getGraphics().dispose();
		}
		
		w = width;
		h = height;
	}
	
	public void setDelay(int delay) {
		this.delay = Math.max(delay, 1);
	}
	
	public void setBase(int index, int delay) {
		baseFrame = index;
		baseDelay = Math.max(delay, 0);
	}
	
	public void setFrame(int index) {
		this.currentFrame = index;
	}
	
	public void nextFrame() {
		if (currentDelay == 0)
			currentFrame = (currentFrame + 1) % frames.length;
		
		if (baseFrame == -1)
			currentDelay = (currentDelay + 1) % delay;
		else
			currentDelay = (currentDelay + 1) % (currentFrame == baseFrame ? baseDelay : delay);
	}
	
	public BufferedImage getCurrentImage() {
		return frames[currentFrame];
	}
	
	// deserialization
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		
		if (type == TYPE_IMAGE)
			this.loadImage(filepath);
		else if (type == TYPE_ANIMATION)
			this.loadAnimatoion(dir, name, ext, frameCount);
		
		this.scale(w, h);
	}
}
