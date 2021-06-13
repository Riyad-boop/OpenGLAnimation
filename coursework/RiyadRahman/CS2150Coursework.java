/* CS2150Coursework.java
 *  180129650, Riyad Kazi Rahman
 *
 * Scene Graph:
 *  Scene origin
 *  |
 *  +-- [Rx(-90) Rxyz(200) Rz(30) T(0.2, 1.5, 0) S(10, 1.0, 10)] MenuBackground
 *  |
 *  +-- [Rx(90) Rz(90) T(20.0, 5.0, 6.5) S(25, 1.0, 20)] BackgroundLeft
 *  |
 *  +-- [Rx(-90) Ry(180) T(7.5, 5.0, 19) S(30.0, 1.0, 20.0)] BackgroundBehind
 *  |
 *  +-- [Rx(180) T(0,20,4) S(40.0, 1.0, 30.0)] BackgroundTop
 *  |
 *  +-- [T(18,12,17)] Moon 
 *  |
 *  +-- [Ry(180) T(0, 0, 10) Rx(PlanePitchAngle) Ry(PlaneYawAngle) Rz(PlaneRollAngle) T(0, 0, PlaneSpeed/1000)] Plane
 *    		|
 *  		+-- [T(0, 0.15, 1.5) S(0.5, 0.5,0 .5)] PlaneList
 * 			|	|
 *     		| 	+-- [] PlaneBody
 *  		|	|
 *     		| 	+-- [] PlanePropellerMount
 *      	|	|
 *      	|	+-- [] PlaneTail
 *      	|
 *      	+-- [T(0, 0.15 ,1.5) S(0.5, 0.5 ,0.5) ] PlaneCockpit
 *          |
 *      	+-- [Rz(PropellerSpeed)] PlanePropellerTop
 *       	|
 *      	+-- [Rz(PropellerSpeed)] PlanePropellerBottom
 *       	|
 *      	+-- [Rx(180) T(-0.8,0.5,2.25)  S(0.75, 0.75, 0.75) ] PlaneRightWing
 *      	|
 *      	+-- [Rx(180) Rz(180) T(0.8,-0.25,2.25) S(0.75,0.75,0.75) ] PlaneLeftWing
 *	       	|
 *	       	+-- [Ry(PlaneRudderAngle-90) T(-0.1, 0, -3) S(0.5,0.75,0.75)] PlaneRudder
 *      

 */
package coursework.RiyadRahman;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.glu.Sphere;

import java.awt.Color;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.opengl.Texture;
import GraphicsLab.*;

/**
 * 
 * A flight simulator game interface, with rotating propellers and a rudder
 * ,which resets to normal when there is no rotational movement detected. The
 * project also includes some custom made textures.
 * <p>
 * Controls:
 * <ul>
 * <li>Press the escape key to exit the application.
 * <li>Hold the x, y and z keys to view the scene along the x, y and z axis,
 * respectively
 * <li>While viewing the scene along the x, y or z axis, use the up and down
 * cursor keys to increase or decrease the viewpoint's distance from the scene
 * origin
 * <li>Hold
 * <li>Press the b o the w or s keys to rotate the plane along x axis
 * <li>Hold the a or d keys to rotate the plane along y axis
 * <li>Hold the q or e keys to rotate the plane along z axis
 * <li>Press the f or g keys to spin or stop propellers
 * <li>Press the r key to move the plane forwards *
 * <li>Press the t key to stop forward movement
 * <li>Press the space key to reset the animationr o key to exit menu screen
 * </ul>
 * 
 * @author Riyad K Rahman
 */
public class CS2150Coursework extends GraphicsLab {

	/**
	 * ids for nearest, linear and mipmapped textures for the plane's material
	 * 
	 * @see <a href=
	 *      "https://opengameart.org/content/red-concrete-wall-512px">planeMat</a> |
	 *      author: Tiziana
	 * @see <a href=
	 *      "https://opengameart.org/content/doom-wall-textures-wall521png">planeWingsMat</a>
	 *      | author: metalx1000
	 * @see <a href= "https://opengameart.org/content/glass">planeCockpitMat</a> |
	 *      author: STKRudy85
	 */
	private Texture planeWingsMat, planeCockpitMat, planeMat;
	/**
	 * ids for nearest, linear and mipmapped textures for the night sky and city sky
	 * line
	 * 
	 * @see <a href=
	 *      "https://openclipart.org/detail/262179/futuristic-city-skyline">bgLeft,
	 *      bgRight</a>
	 */
	private Texture bgTop, bgLeft, bgRight;
	/** ids for nearest, linear and mipmapped textures for the menu screen */
	private Texture bgMenu;

	/**
	 * display list id for the ENTIRE PLANE. Note an invalid value will result in an
	 * exception being thrown
	 */
	private final int planeList = 1;
	/** Speed of the entire plane */
	private float PlaneSpeed = 0.0f;
	/** Speed of the propellers */
	private float PropellerSpeed = 0.0f;
	/** controls if the propellers spin or not */
	private boolean PropellerState = true;
	/** controls if the plane moves forward or not */
	private boolean Accelerate = false;
	/** controls plane rotation on z axis */
	private float PlaneRollAngle = 0.0f;
	/** controls plane rotation on z axis */
	private float PlaneYawAngle = 0.0f;
	/** controls plane rotation on z axis */
	private float PlanePitchAngle = 0.0f;
	/** controls rudder rotation on z axis */
	private float PlaneRudderAngle = 0.0f;
	/** represents which way the rudder is facing */
	private boolean isRudderLeft = false;
	/** controls if background is rendered or not */
	private boolean enableMenu = true;
	/** controls if wireMode is rendered or not */
	private boolean enableWireMode = false;

	/**
	 * Handles running the animation, it's speed and the windows name
	 * 
	 * @param args - The value that the function or method expects to have passed to
	 *             it. (this is unused)
	 */
	public static void main(String args[]) {
		new CS2150Coursework().run(WINDOWED, "CS2150 Coursework Submission| Riyad K Rahman - 180129650", 0.025f);
	}

	/**
	 * Initialises the scene and the resources used, for example lighting and
	 * textures.
	 * <p>
	 * The aeroplane model ( {@link #planeList} ) is also grouped in a list here
	 * </p>
	 */
	protected void initScene() throws Exception {

		// load the textures
		planeWingsMat = loadTexture("coursework/RiyadRahman/textures/Wings.bmp");
		planeCockpitMat = loadTexture("coursework/RiyadRahman/textures/Windows.bmp");
		planeMat = loadTexture("coursework/RiyadRahman/textures/Plane.bmp");
		bgTop = loadTexture("coursework/RiyadRahman/textures/NightSky3.bmp");
		bgLeft = loadTexture("coursework/RiyadRahman/textures/NightSky.bmp");
		bgRight = loadTexture("coursework/RiyadRahman/textures/NightSky2.bmp");
		bgMenu = loadTexture("coursework/RiyadRahman/textures/Menu.bmp");

		float globalAmbient[] = { 0.2f, 0.2f, 0.2f, 1f };
		// set the global ambient lighting
		GL11.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT, FloatBuffer.wrap(globalAmbient));

		// the first light for the scene is white...
		float diffuse0[] = { 1.0f, 1.0f, 1.0f, 1.0f };
		// ...with a dim ambient contribution...
		float ambient0[] = { 0.1f, 0.1f, 0.1f, 1.0f };
		// ...and is positioned above and behind camera
		float position0[] = { 18.0f, 10f, 16f, 1.0f };

		// supply OpenGL with the properties for the first light
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_AMBIENT, FloatBuffer.wrap(ambient0));
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_DIFFUSE, FloatBuffer.wrap(diffuse0));
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_SPECULAR, FloatBuffer.wrap(diffuse0));
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, FloatBuffer.wrap(position0));
		// enable the first light
		GL11.glEnable(GL11.GL_LIGHT0);

		// enable lighting calculations
		GL11.glEnable(GL11.GL_LIGHTING);
		// ensure that all MyNormals are re-Normalised after transformations
		// automatically
		GL11.glEnable(GL11.GL_NORMALIZE);

		// Gouraud shading computes the intensity at each vertex of the polygon, based
		// on an averaged normal.
		GL11.glShadeModel(GL11.GL_SMOOTH);

		// build the Plane display list for later use (Groups each component of the
		// plane)
		GL11.glNewList(planeList, GL11.GL_COMPILE);
		{
			PlaneBody();
			PlaneTail();
			PlanePropellerMount();
		}
		GL11.glEndList();

	}

	/**
	 * Checks all keyboard inputs to manage animations the values are then used in
	 * the render scene methods
	 * 
	 * @see #renderScene()
	 */
	protected void checkSceneInput() {

		// checks to see if you enable the wireMode
		if (Keyboard.isKeyDown(Keyboard.KEY_TAB) == true) {
			enableWireMode = !enableWireMode;
		}

		// checks to see if you disable the menu
		if (Keyboard.isKeyDown(Keyboard.KEY_B) == true) {
			enableMenu = false;
		} else if (Keyboard.isKeyDown(Keyboard.KEY_O) == true) {
			enableMenu = true;
		}

		// checks to see if you turned on the engines
		if (Keyboard.isKeyDown(Keyboard.KEY_F) == true) {
			PropellerState = true;
		} else if (Keyboard.isKeyDown(Keyboard.KEY_G) == true) {
			PropellerState = false;
		}

		// Checks if the propellers are rotating
		if (PropellerState == true) {
			PropellerSpeed -= 15.0f * getAnimationScale(); // Make the Propellers Spin

			// checks to see if you turned on the movement animations
			if (Keyboard.isKeyDown(Keyboard.KEY_R) == true) {
				Accelerate = true;
			} else if (Keyboard.isKeyDown(Keyboard.KEY_T) == true) {
				Accelerate = false;
			}

			// Checks if animations are enabled to move the plane
			if (Accelerate == true) {
				PlaneSpeed += 0.05f; // Moves the Plane forward by 0.05f
			}

			// Make the plane roll RIGHT if the D key is pressed
			if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
				PlaneRollAngle -= 5.0f * getAnimationScale(); // Make the plane roll

				// Slows propellers to reduce wagon-wheel illusion
				if (PlanePitchAngle < 90.0f || PlanePitchAngle > -90.0f) {
					PropellerSpeed += 5.0f * getAnimationScale();
				}

			}

			// Make the plane roll LEFT if the A key is pressed
			if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
				PlaneRollAngle += 5.0f * getAnimationScale(); // Make the plane roll

				// Slows propellers to reduce wagon-wheel illusion
				if (PlanePitchAngle > 90.0f || PlanePitchAngle < -90.0f) {
					PropellerSpeed -= 2.5f * getAnimationScale();
				}

			}

			// Make the plane yaw LEFT if the Q key is pressed
			if (Keyboard.isKeyDown(Keyboard.KEY_Q)) {
				PlaneYawAngle -= 1.25f * getAnimationScale(); // Make the plane yaw left
				PlaneRudderAngle -= 1.25f * getAnimationScale(); // Make the plane rudder turn left
				isRudderLeft = true; // this means rudder is turned left
				if (PlaneRudderAngle < -45) // Force the angle to stick to 45 degrees.
				{
					PlaneRudderAngle = -45f;
				}
			}
			// if the plane is not rotating in y axis then the rudder slowly resets back to
			// 0
			else if (isRudderLeft == true && PlaneRudderAngle < 0) {
				PlaneRudderAngle += 1f * getAnimationScale();
				if (PlaneRudderAngle >= 0) // Force the angle to stick to 0 degrees.
				{
					PlaneRudderAngle = 0f;
				}
			}

			// Make the plane yaw RIGHT if the E key is pressed
			if (Keyboard.isKeyDown(Keyboard.KEY_E)) {
				PlaneYawAngle += 1.25f * getAnimationScale(); // Make the plane yaw right
				PlaneRudderAngle += 1.25f * getAnimationScale(); // Make the plane rudder turn right
				isRudderLeft = false; // this means rudder is turned right
				if (PlaneRudderAngle > 45) // Force the angle to stick to 45 degrees.
				{
					PlaneRudderAngle = 45f;
				}
			}
			// if the plane is not rotating in y axis then the rudder slowly resets back to
			// 0
			else if (isRudderLeft == false && PlaneRudderAngle > 0) {
				PlaneRudderAngle -= 1f * getAnimationScale();
				if (PlaneRudderAngle <= 0) // Force the angle to stick to 0 degrees.
				{
					PlaneRudderAngle = 0f;
				}
			}

			// Make the plane pitch UP if the W key is pressed
			if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
				PlanePitchAngle += 2.5f * getAnimationScale(); // Make the plane pitch up
			}

			// Make the plane pitch DOWN if the S key is pressed
			if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
				PlanePitchAngle -= 2.5f * getAnimationScale(); // Make the plane pitch down
			}
		}

		// Make the plane reset if the SPACE key is pressed
		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			cleanupScene();
		}
	}

	/**
	 * Handles any non-user animations. Prevents plane from going out of range of
	 * the camera
	 */
	protected void updateScene() {

		// if plane goes out of bounds, then stop the plane
		if (PlaneSpeed / 100 >= 6f) {
			PropellerState = false;
		}

	}

	/**
	 * Renders the scene using a scene graph.
	 * 
	 */
	protected void renderScene() {

		// changes to wireMode if user enabled it
		if (enableWireMode == true) {
			GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
		} else {
			GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
		}

		// renders menu if user has it enabled
		if (enableMenu == true) {
			RenderMenu();
		} else {

			RenderBehindBackground();
			RenderLeftBackground();
			RenderTopBackground();

			RenderMoon();

			// This push matrix Encompasses the entire plane 
			GL11.glPushMatrix();
			{
				// position and rotate plane to face camera
				GL11.glTranslatef(0, 0, 10);
				GL11.glRotatef(180f, 0.0f, 1.0f, 0.0f);

				// Rotate entire plane along x axis
				GL11.glRotatef(PlanePitchAngle, 1.0f, 0.0f, 0.0f);
				// Rotate entire plane along y axis
				GL11.glRotatef(PlaneYawAngle, 0.0f, 1.0f, 0.0f);
				// Rotate entire plane along z axis
				GL11.glRotatef(PlaneRollAngle, 0.0f, 0.0f, 1.0f);
				// moves the plane
				GL11.glTranslatef(0f, 0f, (PlaneSpeed / 100));

				// draws the plane's components
				RenderPropellers();
				RenderPlaneBody();
				RenderPlaneCockPit();
				RenderPlaneWings();
				RenderPlaneRudder();

			}
			GL11.glPopMatrix();
		}
	}

	/**
	 * Handles rendering of the moon. This includes setting lighting,textures,
	 * positioning and scaling
	 */
	private void RenderMoon() {
		GL11.glPushMatrix();
		{
			// ++++ no emmision
			float noEmission[] = { 0.0f, 0.0f, 0.0f, 1.0f };

			// Brightness of the moon - blue tinge to make it fit in with blue background
			float[] moonEmission = { 0.4f, 0.4f, 0.6f, 1.0f };
			// how shiny are the front faces of the moon (Specular exponent)
			float moonFrontShine = 2.0f;
			// Specular reflection of the front faces of the moon - blue tinge to make it
			// fit in with blue background
			float moonFrontSpecular[] = { 0.1f, 0.1f, 0.8f, 1.0f };
			// diffuse reflection of the front faces of the moon
			float moonFrontDiffuse[] = { 1.0f, 1.0f, 1.0f, 1.0f };

			// set the material properties for the moon using OpenGL
			GL11.glMaterialf(GL11.GL_FRONT, GL11.GL_SHININESS, moonFrontShine);
			GL11.glMaterial(GL11.GL_FRONT, GL11.GL_SPECULAR, FloatBuffer.wrap(moonFrontSpecular));
			GL11.glMaterial(GL11.GL_FRONT, GL11.GL_DIFFUSE, FloatBuffer.wrap(moonFrontDiffuse));
			GL11.glMaterial(GL11.GL_FRONT, GL11.GL_EMISSION, FloatBuffer.wrap(moonEmission));
			// position and draw the moon using a sphere quadric object
			GL11.glTranslatef(18.0f, 12.0f, 17.0f);
			new Sphere().draw(1f, 10, 10);
			// GL11.glMaterial(GL11.GL_FRONT, GL11.GL_EMISSION,
			// FloatBuffer.wrap(noEmission));
		}
		GL11.glPopMatrix();
	}

	/**
	 * Handles rendering of the both propellers. This includes setting lighting for
	 * both propellers
	 * 
	 * @see #RenderPropellerTop()
	 * @see #RenderPropellerBottom()
	 */
	private void RenderPropellers() {

		// how shiny are the front faces of the Propeller (specular exponent)
		float propellerFrontShininess = 20.0f;
		// specular reflection of the front faces of the Propeller
		float propellerFrontSpecular[] = { 1.0f, 1.0f, 1.0f, 1.0f };
		// diffuse reflection of the front faces of the Propeller
		float propellerFrontDiffuse[] = { 0.5f, 0.5f, 0.5f, 1.0f };

		float propellerFrontEmissive[] = { 0.0f, 0.0f, 0.0f, 1.0f };

		// set the material properties for the Propeller using OpenGL
		GL11.glMaterialf(GL11.GL_FRONT, GL11.GL_SHININESS, propellerFrontShininess);
		GL11.glMaterial(GL11.GL_FRONT, GL11.GL_SPECULAR, FloatBuffer.wrap(propellerFrontSpecular));
		GL11.glMaterial(GL11.GL_FRONT, GL11.GL_DIFFUSE, FloatBuffer.wrap(propellerFrontDiffuse));
		GL11.glMaterial(GL11.GL_FRONT, GL11.GL_EMISSION, FloatBuffer.wrap(propellerFrontEmissive));

		RenderPropellerTop();
		RenderPropellerBottom();

	}

	/**
	 * Handles rendering of the top propellers. This includes setting
	 * textures,positioning and scaling.
	 * 
	 * @see #PlanePropeller()
	 */
	private void RenderPropellerTop() {
		GL11.glPushMatrix();
		{
			// enable texturing and bind an appropriate texture
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, planeWingsMat.getTextureID());

			GL11.glRotatef(PropellerSpeed, 0.0f, 0.0f, 1.0f);
			PlanePropeller();

			GL11.glDisable(GL11.GL_TEXTURE_2D);
		}
		GL11.glPopMatrix();

	}

	/**
	 * Handles rendering of the bottom propellers. This includes setting
	 * textures,positioning and scaling.
	 * 
	 * @see #PlanePropeller()
	 */
	private void RenderPropellerBottom() {
		GL11.glPushMatrix();
		{
			// enable texturing and bind an appropriate texture
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, planeWingsMat.getTextureID());

			GL11.glTranslatef(0.0f, 0.25f, 6.25f);
			GL11.glRotatef(180f, 1.0f, 0f, 0.0f);
			GL11.glRotatef(-PropellerSpeed, 0.0f, 0.0f, 1.0f);
			PlanePropeller();

			GL11.glDisable(GL11.GL_TEXTURE_2D);
		}
		GL11.glPopMatrix();
	}

	/**
	 * Handles rendering of the entire PlaneBody. This includes setting
	 * lighting,textures, positioning and scaling for all its components
	 * 
	 * @see #PlaneBody()
	 * @see #PlaneTail()
	 * @see #PlanePropellerMount()
	 */
	private void RenderPlaneBody() {

		GL11.glPushMatrix();
		{
			// how shiny are the front faces of the plane (specular exponent)
			float planeFrontShininess = 20.0f;
			// specular reflection of the front faces of the plane
			float planeFrontSpecular[] = { 1.0f, 1.0f, 1.0f, 1.0f };
			// diffuse reflection of the front faces of the plane
			float planeFrontDiffuse[] = { 0.8f, 0.8f, 0.8f, 1.0f };

			float planeFrontEmissive[] = { 0.0f, 0f, 0.0f, 1.0f };

			// set the material properties for the plane using OpenGL
			GL11.glMaterialf(GL11.GL_FRONT, GL11.GL_SHININESS, planeFrontShininess);
			GL11.glMaterial(GL11.GL_FRONT, GL11.GL_SPECULAR, FloatBuffer.wrap(planeFrontSpecular));
			GL11.glMaterial(GL11.GL_FRONT, GL11.GL_DIFFUSE, FloatBuffer.wrap(planeFrontDiffuse));
			GL11.glMaterial(GL11.GL_FRONT, GL11.GL_EMISSION, FloatBuffer.wrap(planeFrontEmissive));
			// enable texturing and bind an appropriate texture
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, planeMat.getTextureID());

			// render plane and set position and scale
			GL11.glTranslatef(0.0f, 0.15f, 1.5f);
			GL11.glScalef(0.5f, 0.5f, 0.5f);
			GL11.glCallList(planeList);

			GL11.glDisable(GL11.GL_TEXTURE_2D);

		}
		GL11.glPopMatrix();
	}

	/**
	 * Handles rendering of the PlaneRudder. This includes setting
	 * lighting,textures, positioning and scaling for all its components.
	 * <p>
	 * This uses {@link #PlanePropeller}, but with a different scale to look like a
	 * rudder of a plane
	 * </p>
	 */
	private void RenderPlaneRudder() {
		GL11.glPushMatrix();
		{
			// how shiny are the front faces of the rudder (specular exponent)
			float rudderFrontShininess = 20.0f;
			// specular reflection of the front faces of the rudder
			float rudderFrontSpecular[] = { 1.0f, 1.0f, 1.0f, 1.0f };
			// diffuse reflection of the front faces of the rudder
			float rudderFrontDiffuse[] = { 0.8f, 0.8f, 0.8f, 1.0f };
			// emissive property of the rudder front faces
			float rudderFrontEmissive[] = { 0.0f, 0f, 0.0f, 1.0f };

			// set the material properties for the rudder using OpenGL
			GL11.glMaterialf(GL11.GL_FRONT, GL11.GL_SHININESS, rudderFrontShininess);
			GL11.glMaterial(GL11.GL_FRONT, GL11.GL_SPECULAR, FloatBuffer.wrap(rudderFrontSpecular));
			GL11.glMaterial(GL11.GL_FRONT, GL11.GL_DIFFUSE, FloatBuffer.wrap(rudderFrontDiffuse));
			GL11.glMaterial(GL11.GL_FRONT, GL11.GL_EMISSION, FloatBuffer.wrap(rudderFrontEmissive));

			// enable texturing and bind an appropriate texture
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glScalef(0.5f, 0.75f, 0.75f);

			// turn rudder when plane rotates, the offset 90 rotates the rudder correctly
			// forwards
			GL11.glRotatef(PlaneRudderAngle - 90, 0.0f, 1.0f, 0.0f);

			GL11.glTranslatef(-0.1f, 0f, -3.0f);

			GL11.glBindTexture(GL11.GL_TEXTURE_2D, planeWingsMat.getTextureID());

			PlanePropeller();

			GL11.glDisable(GL11.GL_TEXTURE_2D);
		}
		GL11.glPopMatrix();
	}

	/**
	 * Handles rendering of the PlaneCockpit. This includes setting
	 * lighting,textures, positioning and scaling.
	 * 
	 * @see #PlaneCockpit()
	 */
	private void RenderPlaneCockPit() {
		GL11.glPushMatrix();
		{
			// how shiny are the front faces of the cockpit cube (specular exponent)
			float cockpitFrontShininess = 20.0f;
			// specular reflection of the front faces of the cockpit
			float cockpitFrontSpecular[] = { 1.0f, 1.0f, 1.0f, 1.0f };
			// diffuse reflection of the front faces of the cockpit
			float cockpitFrontDiffuse[] = { 0.8f, 0.8f, 1f, 1.0f };
			// emissive property of the cockpit front faces - a blue glow
			float cockpitFrontEmissive[] = { 0.0f, 0.0f, 0.2f, 1.0f };

			// set the material properties for the cockpit using OpenGL
			GL11.glMaterialf(GL11.GL_FRONT, GL11.GL_SHININESS, cockpitFrontShininess);
			GL11.glMaterial(GL11.GL_FRONT, GL11.GL_SPECULAR, FloatBuffer.wrap(cockpitFrontSpecular));
			GL11.glMaterial(GL11.GL_FRONT, GL11.GL_DIFFUSE, FloatBuffer.wrap(cockpitFrontDiffuse));
			GL11.glMaterial(GL11.GL_FRONT, GL11.GL_EMISSION, FloatBuffer.wrap(cockpitFrontEmissive));
			// enable texturing and bind an appropriate texture
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, planeCockpitMat.getTextureID());

			// render plane and set position and scale
			GL11.glTranslatef(0.0f, 0.15f, 1.5f);
			GL11.glScalef(0.5f, 0.5f, 0.5f);
			PlaneCockpit();
			GL11.glDisable(GL11.GL_TEXTURE_2D);

		}
		GL11.glPopMatrix();

	}

	/**
	 * Handles rendering of the both wings. This includes setting lighting for both
	 * wings
	 * 
	 * @see #RenderPlaneRightWing()
	 * @see #RenderPlaneLeftWing()
	 */
	private void RenderPlaneWings() {

		// how shiny are the front faces of the wing (specular exponent)
		float wingFrontShininess = 20.0f;
		// specular reflection of the front faces of the wing
		float wingFrontSpecular[] = { 1.0f, 1.0f, 1.0f, 1.0f };
		// diffuse reflection of the front faces of the wing
		float wingFrontDiffuse[] = { 0.8f, 0.8f, 0.8f, 1.0f };
		// emissive property of the wing front faces
		float wingFrontEmissive[] = { 0.0f, 0f, 0.0f, 1.0f };

		// set the material properties for the wing using OpenGL
		GL11.glMaterialf(GL11.GL_FRONT, GL11.GL_SHININESS, wingFrontShininess);
		GL11.glMaterial(GL11.GL_FRONT, GL11.GL_SPECULAR, FloatBuffer.wrap(wingFrontSpecular));
		GL11.glMaterial(GL11.GL_FRONT, GL11.GL_DIFFUSE, FloatBuffer.wrap(wingFrontDiffuse));
		GL11.glMaterial(GL11.GL_FRONT, GL11.GL_EMISSION, FloatBuffer.wrap(wingFrontEmissive));

		GL11.glTranslatef(0f, 0.05f, 0f);
		RenderPlaneRightWing();
		RenderPlaneLeftWing();

	}

	/**
	 * Handles rendering of the right wing. This includes setting
	 * textures,positioning and scaling.
	 * 
	 * @see #PlaneRightWing()
	 */
	private void RenderPlaneRightWing() {
		GL11.glPushMatrix();
		{

			// enable texturing and bind an appropriate texture
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, planeWingsMat.getTextureID());

			// render plane and set position and scale
			GL11.glScalef(0.75f, 0.75f, 0.75f);
			GL11.glTranslatef(-0.8f, 0.5f, 2.25f);
			GL11.glRotatef(180f, 1.0f, 0f, 0.0f);
			PlaneRightWing();

			GL11.glDisable(GL11.GL_TEXTURE_2D);

		}
		GL11.glPopMatrix();
	}

	/**
	 * Handles rendering of the left wing. This includes setting
	 * textures,positioning and scaling.
	 * <p>
	 * The right wing is mirrored and used as the left wing
	 * </p>
	 * 
	 * @see #PlaneRightWing()
	 */
	private void RenderPlaneLeftWing() {
		GL11.glPushMatrix();
		{
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, planeWingsMat.getTextureID());

			// render plane and set position and scale
			GL11.glScalef(0.75f, 0.75f, 0.75f);
			GL11.glTranslatef(0.8f, -0.25f, 2.25f);
			GL11.glRotatef(180f, 0f, 0f, 1.0f);
			GL11.glRotatef(180f, 1.0f, 0f, 0f);
			PlaneRightWing();

			GL11.glDisable(GL11.GL_TEXTURE_2D);
		}
		GL11.glPopMatrix();
	}

	/**
	 * Handles rendering of the behind background. This includes setting
	 * textures,positioning and scaling.
	 * 
	 * @see #drawUnitPlaneBackground()
	 */
	private void RenderBehindBackground() {
		// draw the back plane
		GL11.glPushMatrix();
		{
			// disable lighting calculations so that they don't affect
			// the appearance of the texture
			GL11.glPushAttrib(GL11.GL_LIGHTING_BIT);
			GL11.glDisable(GL11.GL_LIGHTING);
			// change the geometry colour to white so that the texture
			// is bright and details can be seen clearly
			Colour.WHITE.submit();
			// enable texturing and bind an appropriate texture
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, bgRight.getTextureID());

			// position, scale and draw the back plane using drawUnitPlaneBackground()
			GL11.glTranslatef(7.5f, 5f, 19.0f);
			// flip image
			GL11.glRotatef(90.0f, -1.0f, 0.0f, 0.0f);
			GL11.glRotatef(180.0f, 0.0f, 1.0f, 0.0f);

			GL11.glScalef(30.0f, 1.0f, 20.0f);
			drawUnitPlaneBackground();

			// disable textures and reset any local lighting changes
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glPopAttrib();
		}
		GL11.glPopMatrix();

	}

	/**
	 * Handles rendering of the left background. This includes setting
	 * textures,positioning and scaling.
	 * 
	 * @see #drawUnitPlaneBackground()
	 */
	private void RenderLeftBackground() {
		// draw the back plane
		GL11.glPushMatrix();
		{
			// disable lighting calculations so that they don't affect
			// the appearance of the texture
			GL11.glPushAttrib(GL11.GL_LIGHTING_BIT);
			GL11.glDisable(GL11.GL_LIGHTING);
			// change the geometry colour to white so that the texture
			// is bright and details can be seen clearly
			Colour.WHITE.submit();
			// enable texturing and bind an appropriate texture
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, bgLeft.getTextureID());

			// position, scale and draw the back plane using its drawUnitPlaneBackground()
			GL11.glTranslatef(20.0f, 5f, 6.5f);
			GL11.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
			GL11.glRotatef(90.0f, 0.0f, 0.0f, 1.0f);
			GL11.glScalef(25.0f, 1.0f, 20.0f);
			drawUnitPlaneBackground();

			// disable textures and reset any local lighting changes
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glPopAttrib();
		}
		GL11.glPopMatrix();
	}

	/**
	 * Handles rendering of the top background. This includes setting
	 * textures,positioning and scaling.
	 * 
	 * @see #drawUnitPlaneBackground()
	 */
	private void RenderTopBackground() {

		// draw the back plane
		GL11.glPushMatrix();
		{
			// disable lighting calculations so that they don't affect
			// the appearance of the texture
			GL11.glPushAttrib(GL11.GL_LIGHTING_BIT);
			GL11.glDisable(GL11.GL_LIGHTING);
			// change the geometry colour to white so that the texture
			// is bright and details can be seen clearly
			Colour.WHITE.submit();
			// enable texturing and bind an appropriate texture
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, bgTop.getTextureID());

			// position, scale and draw the back plane using its drawUnitPlaneBackground()
			GL11.glTranslatef(0.0f, 20f, 4f);
			GL11.glRotatef(180.0f, 1.0f, 0.0f, 0.0f);
			GL11.glScalef(40.0f, 1.0f, 30.0f);

			drawUnitPlaneBackground();

			// disable textures and reset any local lighting changes
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glPopAttrib();
		}
		GL11.glPopMatrix();

	}

	/**
	 * Handles rendering of the Menu Screen. This includes setting
	 * textures,positioning and scaling.
	 * 
	 * @see #drawUnitPlaneBackground()
	 */
	private void RenderMenu() {

		// draw the back plane
		GL11.glPushMatrix();
		{
			// disable lighting calculations so that they don't affect
			// the appearance of the texture
			GL11.glPushAttrib(GL11.GL_LIGHTING_BIT);
			GL11.glDisable(GL11.GL_LIGHTING);
			// change the geometry colour to white so that the texture
			// is bright and details can be seen clearly
			Colour.WHITE.submit();
			// enable texturing and bind an appropriate texture
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, bgMenu.getTextureID());

			// position, scale and draw the back plane using its drawUnitPlaneBackground()
			GL11.glTranslatef(0.2f, 1.5f, 0f);
			GL11.glRotatef(90.0f, -1.0f, 0.0f, 0.0f);
			GL11.glRotatef(200.0f, 0.0f, 0.0f, 0.0f);
			GL11.glRotatef(30.0f, 0.0f, 0.0f, 1.0f);

			GL11.glScalef(10f, 1.0f, 10f);

			drawUnitPlaneBackground();

			// disable textures and reset any local lighting changes
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glPopAttrib();
		}
		GL11.glPopMatrix();
	}

	/**
	 * Sets default values for the sample's viewpoint and projection settings.
	 */
	protected void setSceneCamera() {
		// call the default behaviour defined in GraphicsLab. This will set a default
		// perspective projection and default camera settings
		super.setSceneCamera();

		// custom camera positioning
		// GL11.gluLookAt(...) is used to modify the camera's position and orientation
		GLU.gluLookAt(-5.0f, -5.0f, -8f, // viewer location (position)
				0.0f, 0f, 0.0f, // view point location (position)
				0.0f, 1.0f, 0.0f); // view-up vector (rotation)
	}

	/**
	 * Resets all the appropriate resource values so that the animation can be
	 * played again
	 */
	protected void cleanupScene() {

		PropellerSpeed = 0.0f;
		PropellerState = true;
		Accelerate = false;
		PlaneRollAngle = 0.0f;
		PlaneYawAngle = 0.0f;
		PlanePitchAngle = 0.0f;
		PlaneSpeed = 0.0f;
		PlaneRudderAngle = 0.0f;
		isRudderLeft = false;
		enableMenu = true;
		enableWireMode = false;

	}

	/**
	 * Draws a Propeller geometry of unit length, width and height. The Propeller
	 * uses the current OpenGL material settings for its appearance
	 */
	private void PlanePropeller() {
		// Front
		Vertex v1 = new Vertex(-0.25f, 0.25f, 3.15f);
		Vertex v2 = new Vertex(0.25f, 0.25f, 3.15f);
		Vertex v3 = new Vertex(0.05f, 1f, 3.15f);
		Vertex v4 = new Vertex(-0.25f, 1.5f, 3.15f);

		// Back
		Vertex v5 = new Vertex(-0.25f, 0.25f, 3.0f);
		Vertex v6 = new Vertex(0.25f, 0.25f, 3.0f);
		Vertex v7 = new Vertex(0.05f, 1f, 3.0f);
		Vertex v8 = new Vertex(-0.25f, 1.5f, 3.0f);

		// draw the FRONT face:
		GL11.glBegin(GL11.GL_POLYGON);
		{
			new MyNormal(v1.toVector(), v2.toVector(), v3.toVector(), v4.toVector()).submit();

			GL11.glTexCoord2f(0f, 0f);
			v1.submit();
			GL11.glTexCoord2f(1f, 0f);
			v2.submit();
			GL11.glTexCoord2f(1f, 0.5f);
			v3.submit();
			GL11.glTexCoord2f(0f, 1f);
			v4.submit();
		}
		GL11.glEnd();

		// draw the BACK face:
		GL11.glBegin(GL11.GL_POLYGON);
		{
			new MyNormal(v8.toVector(), v7.toVector(), v6.toVector(), v5.toVector()).submit();

			GL11.glTexCoord2f(0f, 1f);
			v8.submit();
			GL11.glTexCoord2f(1f, 0.5f);
			v7.submit();
			GL11.glTexCoord2f(1f, 0f);
			v6.submit();
			GL11.glTexCoord2f(0f, 0f);
			v5.submit();
		}
		GL11.glEnd();

		// draw the TOP face:
		GL11.glBegin(GL11.GL_POLYGON);
		{
			new MyNormal(v3.toVector(), v7.toVector(), v8.toVector(), v4.toVector()).submit();

			GL11.glTexCoord2f(0f, 0f);
			v3.submit();
			GL11.glTexCoord2f(1f, 0f);
			v7.submit();
			GL11.glTexCoord2f(1f, 1f);
			v8.submit();
			GL11.glTexCoord2f(0f, 1f);
			v4.submit();
		}
		GL11.glEnd();

		// draw the BOTTOM face:
		GL11.glBegin(GL11.GL_POLYGON);
		{
			new MyNormal(v1.toVector(), v5.toVector(), v6.toVector(), v2.toVector()).submit();

			GL11.glTexCoord2f(0f, 1f);
			v1.submit();
			GL11.glTexCoord2f(0f, 0f);
			v5.submit();
			GL11.glTexCoord2f(1f, 0f);
			v6.submit();
			GL11.glTexCoord2f(1f, 1f);
			v2.submit();
		}
		GL11.glEnd();

		// draw the RIGHT face:
		GL11.glBegin(GL11.GL_POLYGON);
		{
			new MyNormal(v2.toVector(), v6.toVector(), v7.toVector(), v3.toVector()).submit();

			GL11.glTexCoord2f(0f, 0f);
			v2.submit();
			GL11.glTexCoord2f(1f, 0f);
			v6.submit();
			GL11.glTexCoord2f(1f, 1f);
			v7.submit();
			GL11.glTexCoord2f(0f, 1f);
			v3.submit();
		}
		GL11.glEnd();

		// draw the LEFT face:
		GL11.glBegin(GL11.GL_POLYGON);
		{
			new MyNormal(v4.toVector(), v8.toVector(), v5.toVector(), v1.toVector()).submit();

			GL11.glTexCoord2f(0f, 1f);
			v4.submit();
			GL11.glTexCoord2f(1f, 1f);
			v8.submit();
			GL11.glTexCoord2f(1f, 0f);
			v5.submit();
			GL11.glTexCoord2f(0f, 0f);
			v1.submit();
		}
		GL11.glEnd();
	}

	/**
	 * Draws a Cockpit geometry of unit length, width and height. The Cockpit uses
	 * the current OpenGL material settings for its appearance
	 */
	private void PlaneCockpit() {
		// the vertices for the cube (note that all sides have a length of 1)
		Vertex v1 = new Vertex(-0.5f, 0.5f, 2f);
		Vertex v2 = new Vertex(-0.5f, 1.5f, 2f);
		Vertex v3 = new Vertex(0.5f, 1.5f, 2f);
		Vertex v4 = new Vertex(0.5f, 0.5f, 2f);
		Vertex v5 = new Vertex(-0.5f, 0.5f, 0f);
		Vertex v6 = new Vertex(-0.5f, 1.5f, 0f);
		Vertex v7 = new Vertex(0.5f, 1.5f, 0f);
		Vertex v8 = new Vertex(0.5f, 0.5f, 0f);

		// draw the near face:
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

	/**
	 * Draws a PropellerMount geometry of unit length, width and height. The
	 * PropellerMount uses the current OpenGL material settings for its appearance
	 */
	private void PlanePropellerMount() {
		// the vertices for the cube (note that all sides have a length of 1)
		Vertex v1 = new Vertex(-0.35f, -0.35f, 3.25f);
		Vertex v2 = new Vertex(-0.35f, 0.35f, 3.25f);
		Vertex v3 = new Vertex(0.35f, 0.35f, 3.25f);
		Vertex v4 = new Vertex(0.35f, -0.35f, 3.25f);
		Vertex v5 = new Vertex(-0.35f, -0.35f, 3.0f);
		Vertex v6 = new Vertex(-0.35f, 0.35f, 3.0f);
		Vertex v7 = new Vertex(0.35f, 0.35f, 3.0f);
		Vertex v8 = new Vertex(0.35f, -0.35f, 3.0f);

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

	/**
	 * Draws the Plane body geometry of unit length, width and height. The Plane
	 * body uses the current OpenGL material settings for its appearance
	 */
	private void PlaneBody() {

		// Front face
		Vertex v1 = new Vertex(-1f, 1f, 3f);
		Vertex v2 = new Vertex(1f, 1f, 3f);
		Vertex v3 = new Vertex(1f, -1f, 3f);
		Vertex v4 = new Vertex(-1f, -1f, 3f);

		// Back face
		Vertex v5 = new Vertex(-0.25f, 0.25f, -3f);
		Vertex v6 = new Vertex(0.25f, 0.25f, -3f);
		Vertex v7 = new Vertex(0.25f, -0.25f, -3f);
		Vertex v8 = new Vertex(-0.25f, -0.25f, -3f);

		// draw the FRONT face:
		GL11.glBegin(GL11.GL_POLYGON);
		{
			new MyNormal(v2.toVector(), v1.toVector(), v4.toVector(), v3.toVector()).submit();

			GL11.glTexCoord2f(1.0f, 1.0f);
			v2.submit();
			GL11.glTexCoord2f(0.0f, 1.0f);
			v1.submit();
			GL11.glTexCoord2f(0.0f, 0.0f);
			v4.submit();
			GL11.glTexCoord2f(1.0f, 0.0f);
			v3.submit();

		}
		GL11.glEnd();

		// draw the TOP face:
		GL11.glBegin(GL11.GL_POLYGON);
		{
			new MyNormal(v2.toVector(), v6.toVector(), v5.toVector(), v1.toVector()).submit();
			GL11.glTexCoord2f(0.0f, 0.0f);
			v2.submit();
			GL11.glTexCoord2f(1.0f, 0.0f);
			v6.submit();
			GL11.glTexCoord2f(1.0f, 1.0f);
			v5.submit();
			GL11.glTexCoord2f(0.0f, 1.0f);
			v1.submit();
		}
		GL11.glEnd();

		// draw the RIGHT face:
		GL11.glBegin(GL11.GL_POLYGON);
		{
			new MyNormal(v6.toVector(), v2.toVector(), v3.toVector(), v7.toVector()).submit();

			GL11.glTexCoord2f(1.0f, 1.0f);
			v6.submit();
			GL11.glTexCoord2f(0.0f, 1.0f);
			v2.submit();
			GL11.glTexCoord2f(0.0f, 0.0f);
			v3.submit();
			GL11.glTexCoord2f(1.0f, 0.0f);
			v7.submit();

		}
		GL11.glEnd();

		// draw the LEFT face:
		GL11.glBegin(GL11.GL_POLYGON);
		{
			new MyNormal(v1.toVector(), v5.toVector(), v8.toVector(), v4.toVector()).submit();
			GL11.glTexCoord2f(1.0f, 1.0f);
			v1.submit();
			GL11.glTexCoord2f(0.0f, 1.0f);
			v5.submit();
			GL11.glTexCoord2f(0.0f, 0.0f);
			v8.submit();
			GL11.glTexCoord2f(1.0f, 0.0f);
			v4.submit();

		}
		GL11.glEnd();

		// draw the BOTTOM face:
		GL11.glBegin(GL11.GL_POLYGON);
		{
			new MyNormal(v4.toVector(), v8.toVector(), v7.toVector(), v3.toVector()).submit();
			GL11.glTexCoord2f(0.0f, 0.0f);
			v4.submit();
			GL11.glTexCoord2f(0.0f, 1.0f);
			v8.submit();
			GL11.glTexCoord2f(1.0f, 1.0f);
			v7.submit();
			GL11.glTexCoord2f(1.0f, 0.0f);
			v3.submit();

		}
		GL11.glEnd();

		// draw the BACK face:
		// Texture co-ordinates not needed as back face is obstructed by the tail
		GL11.glBegin(GL11.GL_POLYGON);
		{
			new MyNormal(v5.toVector(), v6.toVector(), v7.toVector(), v8.toVector()).submit();
			v5.submit();
			v6.submit();
			v7.submit();
			v8.submit();
		}
		GL11.glEnd();

	}

	/**
	 * Draws the Plane right wing geometry of unit length, width and height. The
	 * Plane body uses the current OpenGL material settings for its appearance
	 */
	private void PlaneRightWing() {
		// Top
		Vertex v1 = new Vertex(0.8f, 0.5f, 1.5f);
		Vertex v2 = new Vertex(0.65f, 0.5f, -0.5f);
		Vertex v3 = new Vertex(6f, 0.5f, 1.5f);
		Vertex v4 = new Vertex(6f, 0.5f, 0.75f);
		Vertex v5 = new Vertex(3f, 0.5f, -0.5f);

		// Bottom
		Vertex v6 = new Vertex(0.8f, 0.25f, 1.5f);
		Vertex v7 = new Vertex(0.65f, 0.25f, -0.5f);
		Vertex v8 = new Vertex(6f, 0.25f, 1.5f);
		Vertex v9 = new Vertex(6f, 0.25f, 0.75f);
		Vertex v10 = new Vertex(3f, 0.25f, -0.5f);

		// draw the TOP face:
		GL11.glBegin(GL11.GL_POLYGON);
		{
			new MyNormal(v3.toVector(), v4.toVector(), v5.toVector(), v2.toVector(), v1.toVector()).submit();

			GL11.glTexCoord2f(0.0f, 0.0f);
			v3.submit();
			GL11.glTexCoord2f(1.0f, 0.0f);
			v4.submit();
			GL11.glTexCoord2f(1.0f, 0.5f);
			v5.submit();
			GL11.glTexCoord2f(1.0f, 1.0f);
			v2.submit();
			GL11.glTexCoord2f(0f, 1.0f);
			v1.submit();
		}
		GL11.glEnd();

		// draw the BACK face:
		GL11.glBegin(GL11.GL_POLYGON);
		{
			new MyNormal(v6.toVector(), v8.toVector(), v3.toVector(), v1.toVector()).submit();

			GL11.glTexCoord2f(0.0f, 0.0f);
			v6.submit();
			GL11.glTexCoord2f(1.0f, 0.0f);
			v8.submit();
			GL11.glTexCoord2f(1.0f, 1.0f);
			v3.submit();
			GL11.glTexCoord2f(0.0f, 1.0f);
			v1.submit();

		}
		GL11.glEnd();

		// draw the RIGHT face:
		GL11.glBegin(GL11.GL_POLYGON);
		{
			new MyNormal(v4.toVector(), v3.toVector(), v8.toVector(), v9.toVector()).submit();

			GL11.glTexCoord2f(0.0f, 1.0f);
			v4.submit();
			GL11.glTexCoord2f(1.0f, 1.0f);
			v3.submit();
			GL11.glTexCoord2f(1.0f, 0.0f);
			v8.submit();
			GL11.glTexCoord2f(0.0f, 0.0f);
			v9.submit();

		}
		GL11.glEnd();

		// draw the BOTTOM face:
		GL11.glBegin(GL11.GL_POLYGON);
		{
			new MyNormal(v6.toVector(), v7.toVector(), v10.toVector(), v9.toVector(), v8.toVector()).submit();

			GL11.glTexCoord2f(0.0f, 0.0f);
			v6.submit();
			GL11.glTexCoord2f(1.0f, 0.0f);
			v7.submit();
			GL11.glTexCoord2f(1.0f, 0.5f);
			v10.submit();
			GL11.glTexCoord2f(1.0f, 1.0f);
			v9.submit();
			GL11.glTexCoord2f(0f, 1.0f);
			v8.submit();
		}
		GL11.glEnd();

		// draw the LEFT face: (NOT NEEEDED)
		GL11.glBegin(GL11.GL_POLYGON);
		{
			new MyNormal(v6.toVector(), v1.toVector(), v2.toVector(), v7.toVector()).submit();
			v6.submit();
			v1.submit();
			v2.submit();
			v7.submit();

		}
		GL11.glEnd();

		// draw the FRONT face LEFT DIAGONAL:
		GL11.glBegin(GL11.GL_POLYGON);
		{
			new MyNormal(v5.toVector(), v4.toVector(), v9.toVector(), v10.toVector()).submit();
			GL11.glTexCoord2f(0.0f, 1.0f);
			v5.submit();
			GL11.glTexCoord2f(1.0f, 1.0f);
			v4.submit();
			GL11.glTexCoord2f(1.0f, 0.0f);
			v9.submit();
			GL11.glTexCoord2f(0.0f, 0.0f);
			v10.submit();

		}
		GL11.glEnd();

		// draw the FRONT face RIGHT DIAGONAL:
		GL11.glBegin(GL11.GL_POLYGON);
		{
			new MyNormal(v2.toVector(), v5.toVector(), v10.toVector(), v7.toVector()).submit();
			GL11.glTexCoord2f(0.0f, 1.0f);
			v2.submit();
			GL11.glTexCoord2f(1.0f, 1.0f);
			v5.submit();
			GL11.glTexCoord2f(1.0f, 0.0f);
			v10.submit();
			GL11.glTexCoord2f(0.0f, 0.0f);
			v7.submit();
		}
		GL11.glEnd();

	}

	/**
	 * Draws the plane tail geometry of unit length, width and height. The plane
	 * tail uses the current OpenGL material settings for its appearance
	 */
	private void PlaneTail() {
		// Top
		Vertex v1 = new Vertex(0.5f, 0.25f, -3f);
		Vertex v2 = new Vertex(1.5f, 0.25f, -3.2f);
		Vertex v3 = new Vertex(1.5f, 0.25f, -3.5f);
		Vertex v4 = new Vertex(-1.5f, 0.25f, -3.5f);
		Vertex v5 = new Vertex(-1.5f, 0.25f, -3.2f);
		Vertex v6 = new Vertex(-0.5f, 0.25f, -3f);

		// Bottom
		Vertex v7 = new Vertex(0.5f, -0.25f, -3f);
		Vertex v8 = new Vertex(1.5f, -0.25f, -3.2f);
		Vertex v9 = new Vertex(1.5f, -0.25f, -3.5f);
		Vertex v10 = new Vertex(-1.5f, -0.25f, -3.5f);
		Vertex v11 = new Vertex(-1.5f, -0.25f, -3.2f);
		Vertex v12 = new Vertex(-0.5f, -0.25f, -3f);

		// draw the TOP face:
		GL11.glBegin(GL11.GL_POLYGON);
		{
			new MyNormal(v1.toVector(), v2.toVector(), v3.toVector(), v4.toVector(), v5.toVector(), v6.toVector())
					.submit();
			GL11.glTexCoord2f(0.25f, 0.0f);
			v1.submit();
			GL11.glTexCoord2f(0.0f, 0.5f);
			v2.submit();
			GL11.glTexCoord2f(0.0f, 1.0f);
			v3.submit();
			GL11.glTexCoord2f(1.0f, 1.0f);
			v4.submit();
			GL11.glTexCoord2f(1.0f, 0.5f);
			v5.submit();
			GL11.glTexCoord2f(0.5f, 0.0f);
			v6.submit();
		}
		GL11.glEnd();

		// draw the FRONT face:
		GL11.glBegin(GL11.GL_POLYGON);
		{
			new MyNormal(v1.toVector(), v6.toVector(), v12.toVector(), v7.toVector()).submit();
			GL11.glTexCoord2f(0.0f, 1.0f);
			v1.submit();
			GL11.glTexCoord2f(1.0f, 1.0f);
			v6.submit();
			GL11.glTexCoord2f(1.0f, 0.0f);
			v12.submit();
			GL11.glTexCoord2f(0.0f, 0.0f);
			v7.submit();

		}
		GL11.glEnd();

		// draw the FRONT face LEFT DIAGONAL:
		GL11.glBegin(GL11.GL_POLYGON);
		{
			new MyNormal(v2.toVector(), v1.toVector(), v7.toVector(), v8.toVector()).submit();
			GL11.glTexCoord2f(0.0f, 1.0f);
			v2.submit();
			GL11.glTexCoord2f(1.0f, 1.0f);
			v1.submit();
			GL11.glTexCoord2f(1.0f, 0.0f);
			v7.submit();
			GL11.glTexCoord2f(0.0f, 0.0f);
			v8.submit();

		}
		GL11.glEnd();

		// draw the FRONT face RIGHT DIAGONAL:
		GL11.glBegin(GL11.GL_POLYGON);
		{
			new MyNormal(v6.toVector(), v5.toVector(), v11.toVector(), v12.toVector()).submit();
			GL11.glTexCoord2f(0.0f, 1.0f);
			v6.submit();
			GL11.glTexCoord2f(1.0f, 1.0f);
			v5.submit();
			GL11.glTexCoord2f(1.0f, 0.0f);
			v11.submit();
			GL11.glTexCoord2f(0.0f, 0.0f);
			v12.submit();

		}
		GL11.glEnd();

		// draw the RIGHT face:
		GL11.glBegin(GL11.GL_POLYGON);
		{
			new MyNormal(v5.toVector(), v4.toVector(), v10.toVector(), v11.toVector()).submit();
			GL11.glTexCoord2f(0.0f, 1.0f);
			v5.submit();
			GL11.glTexCoord2f(1.0f, 1.0f);
			v4.submit();
			GL11.glTexCoord2f(1.0f, 0.0f);
			v10.submit();
			GL11.glTexCoord2f(0.0f, 0.0f);
			v11.submit();

		}
		GL11.glEnd();

		// draw the LEFT face:
		GL11.glBegin(GL11.GL_POLYGON);
		{
			new MyNormal(v9.toVector(), v3.toVector(), v2.toVector(), v8.toVector()).submit();
			GL11.glTexCoord2f(0.0f, 0.0f);
			v9.submit();
			GL11.glTexCoord2f(0.0f, 1.0f);
			v3.submit();
			GL11.glTexCoord2f(1.0f, 1.0f);
			v2.submit();
			GL11.glTexCoord2f(0.0f, 0.0f);
			v8.submit();
		}
		GL11.glEnd();

		// draw the BACK face:
		GL11.glBegin(GL11.GL_POLYGON);
		{
			new MyNormal(v9.toVector(), v10.toVector(), v4.toVector(), v3.toVector()).submit();
			GL11.glTexCoord2f(0.0f, 0.0f);
			v9.submit();
			GL11.glTexCoord2f(0.0f, 0.0f);
			v10.submit();
			GL11.glTexCoord2f(1.0f, 1.0f);
			v4.submit();
			GL11.glTexCoord2f(0.0f, 1.0f);
			v3.submit();

		}
		GL11.glEnd();

		// draw the BOTTOM face:
		GL11.glBegin(GL11.GL_POLYGON);
		{
			new MyNormal(v12.toVector(), v11.toVector(), v10.toVector(), v9.toVector(), v8.toVector(), v7.toVector())
					.submit();
			GL11.glTexCoord2f(0.5f, 0.0f);
			v12.submit();
			GL11.glTexCoord2f(1.0f, 0.5f);
			v11.submit();
			GL11.glTexCoord2f(1.0f, 1.0f);
			v10.submit();
			GL11.glTexCoord2f(0.0f, 1.0f);
			v9.submit();
			GL11.glTexCoord2f(0.0f, 0.5f);
			v8.submit();
			GL11.glTexCoord2f(0.25f, 0.0f);
			v7.submit();
		}
		GL11.glEnd();

	}

	/**
	 * draw the plane geometry. order the vertices so that the plane faces up
	 */
	private void drawUnitPlaneBackground() {
		Vertex v1 = new Vertex(-0.5f, 0.0f, -0.5f); // left, back
		Vertex v2 = new Vertex(0.5f, 0.0f, -0.5f); // right, back
		Vertex v3 = new Vertex(0.5f, 0.0f, 0.5f); // right, front
		Vertex v4 = new Vertex(-0.5f, 0.0f, 0.5f); // left, front

		// draw the plane geometry. order the vertices so that the plane faces up
		GL11.glBegin(GL11.GL_POLYGON);
		{
			new Normal(v4.toVector(), v3.toVector(), v2.toVector(), v1.toVector()).submit();

			GL11.glTexCoord2f(0.0f, 0.0f);
			v4.submit();

			GL11.glTexCoord2f(1.0f, 0.0f);
			v3.submit();

			GL11.glTexCoord2f(1.0f, 1.0f);
			v2.submit();

			GL11.glTexCoord2f(0.0f, 1.0f);
			v1.submit();
		}
		GL11.glEnd();

		// if the user is viewing an axis, then also draw this plane
		// using lines so that axis aligned planes can still be seen
		if (isViewingAxis()) {
			// also disable textures when drawing as lines
			// so that the lines can be seen more clearly
			GL11.glPushAttrib(GL11.GL_TEXTURE_2D);
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glBegin(GL11.GL_LINE_LOOP);
			{
				v4.submit();
				v3.submit();
				v2.submit();
				v1.submit();
			}
			GL11.glEnd();
			GL11.glPopAttrib();
		}
	}
}
