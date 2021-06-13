/* CS2150Coursework.java
 * TODO: put your university username and full name here
 *
 * Scene Graph:
 *  Scene origin
 *  |
 *
 *  TODO: Provide a scene graph for your submission
 */
package exam.RiyadRahman;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.glu.Sphere;
import org.lwjgl.input.Keyboard;
import org.newdawn.slick.opengl.Texture;
import GraphicsLab.*;

/**
 * TODO: Briefly describe your submission here
 *
 * <p>
 * Controls:
 * <ul>
 * <li>Press the escape key to exit the application.
 * <li>Hold the x, y and z keys to view the scene along the x, y and z axis,
 * respectively
 * <li>While viewing the scene along the x, y or z axis, use the up and down
 * cursor keys to increase or decrease the viewpoint's distance from the scene
 * origin
 * </ul>
 * TODO: Add any additional controls for your sample to the list above
 *
 */
public class CS2150RiyadExam1a2 extends GraphicsLab {

	/** true or false value for swing */
	private boolean Swing;
	/** true or false value for turning robot */
	private boolean turnLeft;
	/** true or false value for turning robot */
	private boolean turnRight;
	/** Rotation of Pendulum */
	private float Rotation = 0.0f;
	/** Max Rotation of Pendulum */
	private static final float maxAngle = 30.0f;
	/** Min Rotation of Pendulum */
	private static final float minAngle = -30.0f;
	/** count of cycles */
	private int count;

	// TODO: Feel free to change the window title and default animation scale here
	public static void main(String args[]) {
		new CS2150RiyadExam1a2().run(WINDOWED, "CS2150 Coursework Submission", 0.01f);
	}

	protected void initScene() throws Exception {
		Swing = true;
		Rotation = 0.0f;
		turnRight= true;
		count = 0;

		float globalAmbientLight[] = { 0.35f, 0.35f, 0.35f, 1f };
		// set the global ambient lighting
		GL11.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT, FloatBuffer.wrap(globalAmbientLight));

		// the first light (diffuse) for the scene is white
		float diffuse0[] = { 1.0f, 1.0f, 1.0f, 1.0f };
		// the ambient is set to a dim white
		float ambient0[] = { 0.1f, 0.1f, 0.1f, 1.0f };
		// it is positioned above and behind camera
		float position0[] = { 15.0f, 10f, 16f, 1.0f };

		// supply OpenGL with the properties for the first light
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, FloatBuffer.wrap(position0));
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_AMBIENT, FloatBuffer.wrap(ambient0));
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_SPECULAR, FloatBuffer.wrap(diffuse0));
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_DIFFUSE, FloatBuffer.wrap(diffuse0));

		// enable the first light
		GL11.glEnable(GL11.GL_LIGHT0);

		// enable lighting calculations
		GL11.glEnable(GL11.GL_LIGHTING);
		// ensure that all MyNormals are re-Normalised after transformations
		// automatically
		GL11.glEnable(GL11.GL_NORMALIZE);

		// The Gouraud shading technique computes the intensity at each vertex of the
		// polygon,
		// based on an averaged normal.
		GL11.glShadeModel(GL11.GL_SMOOTH);
	}

	protected void checkSceneInput() {
		if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			turnLeft = true;
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			turnRight = true;
		}

	}

	protected void updateScene() {

		if (Swing == true) {

			if (turnLeft == true) {
				Rotation += 2f * getAnimationScale();
			}

			if (turnRight == true) {
				Rotation -= 2f * getAnimationScale();
			}

			if (Rotation >= maxAngle) {
				turnLeft = false;
				turnRight = true;
				count++;
			}

			if (Rotation <= minAngle) {
				turnRight = false;
				turnLeft = true;
				count++;
			}

		}

		// if the swinging stopped then the pendulum
		else {
			// if the rotation angle isn't already 0 
			// then it should be reset to 0 
			// by slowly decrementing down
			if (Rotation != 0) {
				
				//swing it left if final position was on right
				if (Rotation > 0) {
					Rotation -= 2f * getAnimationScale();
				}
				
				//swing it right if final position was on left
				if (Rotation < 0) {
					Rotation += 2f * getAnimationScale();
				}
			}
			
		}

		if (count >= 3) {
			Swing = false;
		}

	}

	protected void renderScene() {
		GL11.glPushMatrix();
		{

			GL11.glTranslatef(0f,2.5f, 0f);
			GL11.glRotatef(180f, 0.0f, 0.0f, 1.0f);
			GL11.glTranslatef(0f,1f, 0f);
			GL11.glRotatef(Rotation, 0.0f, 0.0f, 1.0f);
			GL11.glTranslatef(0f,-1f, 0f);
			
			drawPendulum();
			

		}
		GL11.glPopMatrix();
	}

	protected void drawPendulum() {
			
		// pendulum string
		GL11.glBegin(GL11.GL_LINES);
		GL11.glVertex3i(0, 5, 1);
		GL11.glVertex3i(0, 1, 1);
		GL11.glEnd();
		

		// sphere of the pendulum
		GL11.glPushMatrix();
		{
		
			GL11.glTranslatef(0f, 5.5f, 1f);
			new Sphere().draw(0.5f, 10, 10);
		}
		GL11.glPopMatrix();
	}

	protected void setSceneCamera() {
		// call the default behaviour defined in GraphicsLab. This will set a default
		// perspective projection
		// and default camera settings ready for some custom camera positioning below...
		super.setSceneCamera();

		// custom camera positioning
		// GL11.gluLookAt(...) is used to modify the camera's position and orientation
		GLU.gluLookAt(-5.0f, -2.5f, -8f, // viewer location (position)
				0.0f, 0f, 0.0f, // view point location (position)
				0.0f, 1.0f, 0.0f); // view-up vector (rotation)
	}

	protected void cleanupScene() {// TODO: Clean up your resources here
	}

}