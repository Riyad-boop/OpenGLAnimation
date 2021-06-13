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
public class CS2150RiyadExam1a extends GraphicsLab {

	/** Speed of the entire robot */
	private float movementSpeed = 0.0f;
	/** true or false value for walking animation */
	private boolean walk = true;
	/** Rotation of legs */
	private float legRotation = 0.0f;
	/** true or false value for switching legs */
	private boolean myswtich = false;
	/** true or false value for turning robot */
	private boolean turnLeft = true;
	/** true or false value for turning robot */
	private boolean turnRight = false;
	/** Rotation of legs */
	private float robotRotation = 0.0f;
	/** ids for nearest, linear and mipmapped textures for the entire robot */
	private Texture robotTexture;

	// TODO: Feel free to change the window title and default animation scale here
	public static void main(String args[]) {
		new CS2150RiyadExam1a().run(WINDOWED, "CS2150 Coursework Submission", 0.01f);
	}

	protected void initScene() throws Exception {

		// load the textures
		robotTexture = loadTexture("exam/RiyadRahman/textures/Metal.bmp");

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

	}

	protected void updateScene() {

		// Make the robot walk forward if walk is true
		if (walk = true) {
			movementSpeed += 0.15f * getAnimationScale(); // Make the robot move forward

			/**
			 * Leg
			 */
			// when switch is false the legs rotate clockwise
			if (myswtich == false) {
				legRotation += 2f * getAnimationScale();
			}
			// when switch is true the legs rotate anti-clockwise
			else if (myswtich == true) {
				legRotation -= 2f * getAnimationScale();
			}
		}

		// switches leg rotation when it's at 40 degrees or greater
		if (legRotation >= 40f) {
			myswtich = true;
		}
		// switches leg rotation when it's at -40 degrees or weaker
		else if (legRotation <= -40f) {
			myswtich = false;
		}

		/**
		 * rotation of robot
		 */
		// if the robot is walking
		if (turnLeft == true) {
			robotRotation += 2f * getAnimationScale();
			// stops rotating if it is greater than or equal to than -60 degrees
			if (robotRotation >= 60f) {
				robotRotation = 60f;
				turnLeft = false;
				turnRight = true;
			}
		}

		// if the robot is walking
		if (turnRight == true) {
			robotRotation -= 2f * getAnimationScale();

			// stops rotating if it is less than or equal to than -60 degrees
			if (robotRotation <= -60f) {
				robotRotation = -60f;
				turnRight = false;
				turnLeft = true;
			}
		}

	}

	protected void renderScene() {

		// enable textures
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, robotTexture.getTextureID());

		drawRobot();

		GL11.glDisable(GL11.GL_TEXTURE_2D);
	}

	private void drawRobot() {
		/**
		 * push matrix which encapsulates whole robot this means any transformations in
		 * this push matrix will be applied to all of the robot parts
		 */
		GL11.glPushMatrix();
		{

			GL11.glTranslatef(0f, 0f, movementSpeed);

			// head of the robot
			GL11.glPushMatrix();
			{
				GL11.glRotatef(robotRotation, 0.0f, 1.0f, 0.0f);
				GL11.glTranslatef(0f, 3f, 0f);
				GL11.glScaled(1.25f, 1.0f, 1.0f);
				drawCube();
			}
			GL11.glPopMatrix();

			// body of the robot
			GL11.glPushMatrix();
			{
				GL11.glTranslatef(0f, 1f, 0f);
				GL11.glScaled(2.0f, 3.0f, 1.0f);
				drawCube();
			}
			GL11.glPopMatrix();

			// right leg of the robot
			GL11.glPushMatrix();
			{
				GL11.glRotatef(legRotation, 1.0f, 0.0f, 0.0f);
				GL11.glTranslatef(0.50f, -2f, 0f);
				GL11.glScaled(0.65f, 2.5f, 1.0f);
				drawCube();
			}
			GL11.glPopMatrix();

			// left leg of the robot
			GL11.glPushMatrix();
			{
				GL11.glRotatef(-legRotation, 1.0f, 0.0f, 0.0f);
				GL11.glTranslatef(-0.50f, -2f, 0f);
				GL11.glScaled(0.65f, 2.5f, 1.0f);
				drawCube();
			}
			GL11.glPopMatrix();

			// left arm of the robot
			GL11.glPushMatrix();
			{
				GL11.glRotatef(10.0f, 0.0f, 0.0f, 1.0f);
				GL11.glTranslatef(1.75f, 0.75f, 0f);
				GL11.glScaled(0.65f, 2.5f, 1.0f);

				drawCube();
			}
			GL11.glPopMatrix();

			// right arm of the robot
			GL11.glPushMatrix();
			{
				GL11.glRotatef(10.0f, 0.0f, 0.0f, -1.0f);
				GL11.glTranslatef(-1.75f, 0.75f, 0f);
				GL11.glScaled(0.65f, 2.5f, 1.0f);

				drawCube();
			}
			GL11.glPopMatrix();
		}
		GL11.glPopMatrix();
	}

	private void drawCube() {

		/**
		 * the vertices for the cube where every line or edge has length 1 this makes it
		 * easier to perform translations and scales
		 */

		Vertex v1 = new Vertex(-0.5f, -0.5f, 0.5f);
		Vertex v2 = new Vertex(-0.5f, 0.5f, 0.5f);
		Vertex v3 = new Vertex(0.5f, 0.5f, 0.5f);
		Vertex v4 = new Vertex(0.5f, -0.5f, 0.5f);
		Vertex v5 = new Vertex(-0.5f, -0.5f, -0.5f);
		Vertex v6 = new Vertex(-0.5f, 0.5f, -0.5f);
		Vertex v7 = new Vertex(0.5f, 0.5f, -0.5f);
		Vertex v8 = new Vertex(0.5f, -0.5f, -0.5f);

		GL11.glBegin(GL11.GL_POLYGON);
		{
			new Normal(v3.toVector(), v2.toVector(), v1.toVector(), v4.toVector()).submit();

			GL11.glTexCoord2f(1.0f, 1.0f);
			v3.submit();
			GL11.glTexCoord2f(0.0f, 1.0f);
			v2.submit();
			GL11.glTexCoord2f(0.0f, 0.0f);
			v1.submit();
			GL11.glTexCoord2f(1.0f, 0.0f);
			v4.submit();
		}
		GL11.glEnd();

		// draw the left face:
		GL11.glBegin(GL11.GL_POLYGON);
		{
			new Normal(v2.toVector(), v6.toVector(), v5.toVector(), v1.toVector()).submit();

			GL11.glTexCoord2f(1.0f, 1.0f);
			v2.submit();
			GL11.glTexCoord2f(0.0f, 1.0f);
			v6.submit();
			GL11.glTexCoord2f(0.0f, 0.0f);
			v5.submit();
			GL11.glTexCoord2f(1.0f, 0.0f);
			v1.submit();
		}
		GL11.glEnd();

		// draw the right face:
		GL11.glBegin(GL11.GL_POLYGON);
		{
			new Normal(v7.toVector(), v3.toVector(), v4.toVector(), v8.toVector()).submit();

			GL11.glTexCoord2f(1.0f, 1.0f);
			v7.submit();
			GL11.glTexCoord2f(0.0f, 1.0f);
			v3.submit();
			GL11.glTexCoord2f(0.0f, 0.0f);
			v4.submit();
			GL11.glTexCoord2f(1.0f, 0.0f);
			v8.submit();
		}
		GL11.glEnd();

		// draw the top face:
		GL11.glBegin(GL11.GL_POLYGON);
		{
			new Normal(v7.toVector(), v6.toVector(), v2.toVector(), v3.toVector()).submit();

			GL11.glTexCoord2f(1.0f, 1.0f);
			v7.submit();
			GL11.glTexCoord2f(0.0f, 1.0f);
			v6.submit();
			GL11.glTexCoord2f(0.0f, 0.0f);
			v2.submit();
			GL11.glTexCoord2f(1.0f, 0.0f);
			v3.submit();
		}
		GL11.glEnd();

		// draw the bottom face:
		GL11.glBegin(GL11.GL_POLYGON);
		{
			new Normal(v4.toVector(), v1.toVector(), v5.toVector(), v8.toVector()).submit();

			GL11.glTexCoord2f(1.0f, 1.0f);
			v4.submit();
			GL11.glTexCoord2f(0.0f, 1.0f);
			v1.submit();
			GL11.glTexCoord2f(0.0f, 0.0f);
			v5.submit();
			GL11.glTexCoord2f(1.0f, 0.0f);
			v8.submit();
		}
		GL11.glEnd();

		// draw the far face:
		GL11.glBegin(GL11.GL_POLYGON);
		{
			new Normal(v6.toVector(), v7.toVector(), v8.toVector(), v5.toVector()).submit();

			GL11.glTexCoord2f(1.0f, 1.0f);
			v6.submit();
			GL11.glTexCoord2f(0.0f, 1.0f);
			v7.submit();
			GL11.glTexCoord2f(0.0f, 0.0f);
			v8.submit();
			GL11.glTexCoord2f(1.0f, 0.0f);
			v5.submit();
		}
		GL11.glEnd();
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