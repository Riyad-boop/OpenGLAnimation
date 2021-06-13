package coursework.RiyadRahman;


import org.lwjgl.opengl.GL11;
import GraphicsLab.Vector;

/**
 * Encapsulates the concept of a 3D normal
 * 
 * @author Riyad Rahman and Anthony Jones and Dan Cornford 
 */
public class MyNormal {

	/**
	 * Constructs a MyNormal object from its x, y and z components
	 * 
	 * @param x The MyNormal's x component
	 * @param y The MyNormal's y component
	 * @param z The MyNormal's z component
	 */
	public MyNormal(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
		MyNormalise();
	}

	/**
	 * Constructs a MyNormal vector by taking 3 co-planar points (not co-linear):
	 * Based on computing two vectors in the plane, then taking the vector cross
	 * product between these to compute the MyNormal; to get the sign right the
	 * vectors must be passed in anti-clockwise order.
	 * 
	 * @param vec1
	 * @param vec2
	 * @param vec3
	 */
	public MyNormal(Vector vec1, Vector vec2, Vector vec3) {
		// we will hard-wire this - it is not pretty or clever, but easily understood.
		// in theory we should pass in an array of vectors and compute these terms in a
		// loop!
		x = (vec1.getY() - vec2.getY()) * (vec1.getZ() + vec2.getZ())
				+ (vec2.getY() - vec3.getY()) * (vec2.getZ() + vec3.getZ())
				+ (vec3.getY() - vec1.getY()) * (vec3.getZ() + vec1.getZ());
		y = (vec1.getZ() - vec2.getZ()) * (vec1.getX() + vec2.getX())
				+ (vec2.getZ() - vec3.getZ()) * (vec2.getX() + vec3.getX())
				+ (vec3.getZ() - vec1.getZ()) * (vec3.getX() + vec1.getX());
		z = (vec1.getX() - vec2.getX()) * (vec1.getY() + vec2.getY())
				+ (vec2.getX() - vec3.getX()) * (vec2.getY() + vec3.getY())
				+ (vec3.getX() - vec1.getX()) * (vec3.getY() + vec1.getY());
		// now MyNormalise the result to have length 1
		MyNormalise();
	}

	/**
	 * Constructs a MyNormal vector by taking 4 co-planar points (not co-linear):
	 * Based on computing two vectors in the plane, then taking the vector cross
	 * product between these to compute the MyNormal; to get the sign right the
	 * vectors must be passed in anti-clockwise order.
	 * 
	 * @param vec1
	 * @param vec2
	 * @param vec3
	 * @param vec4
	 */
	public MyNormal(Vector vec1, Vector vec2, Vector vec3, Vector vec4) {
		// we will hard-wire this - it is not pretty or clever, but easily understood.
		// in theory we should pass in an array of vectors and compute these terms in a
		// loop!
		x = (vec1.getY() - vec2.getY()) * (vec1.getZ() + vec2.getZ())
				+ (vec2.getY() - vec3.getY()) * (vec2.getZ() + vec3.getZ())
				+ (vec3.getY() - vec4.getY()) * (vec3.getZ() + vec4.getZ())
				+ (vec4.getY() - vec1.getY()) * (vec4.getZ() + vec1.getZ());
		y = (vec1.getZ() - vec2.getZ()) * (vec1.getX() + vec2.getX())
				+ (vec2.getZ() - vec3.getZ()) * (vec2.getX() + vec3.getX())
				+ (vec3.getZ() - vec4.getZ()) * (vec3.getX() + vec4.getX())
				+ (vec4.getZ() - vec1.getZ()) * (vec4.getX() + vec1.getX());
		z = (vec1.getX() - vec2.getX()) * (vec1.getY() + vec2.getY())
				+ (vec2.getX() - vec3.getX()) * (vec2.getY() + vec3.getY())
				+ (vec3.getX() - vec4.getX()) * (vec3.getY() + vec4.getY())
				+ (vec4.getX() - vec1.getX()) * (vec4.getY() + vec1.getY());
		// now MyNormalise the result to have length 1
		MyNormalise();
	}

	/**
	 * Constructs a MyNormal vector by taking 5 co-planar points (not co-linear):
	 * Based on computing two vectors in the plane, then taking the vector cross
	 * product between these to compute the MyNormal; to get the sign right the
	 * vectors must be passed in anti-clockwise order.
	 * 
	 * @author RIYAD K RAHMAN
	 * @param vec1
	 * @param vec2
	 * @param vec3
	 * @param vec4
	 * @param vec5
	 */
	public MyNormal(Vector vec1, Vector vec2, Vector vec3, Vector vec4, Vector vec5) {
		// we will hard-wire this - it is not pretty or clever, but easily understood.
		// in theory we should pass in an array of vectors and compute these terms in a
		// loop!
		x = (vec1.getY() - vec2.getY()) * (vec1.getZ() + vec2.getZ())
				+ (vec2.getY() - vec3.getY()) * (vec2.getZ() + vec3.getZ())
				+ (vec3.getY() - vec4.getY()) * (vec3.getZ() + vec4.getZ())
				+ (vec4.getY() - vec5.getY()) * (vec4.getZ() + vec5.getZ())
				+ (vec5.getY() - vec1.getY()) * (vec5.getZ() + vec1.getZ());

		y = (vec1.getZ() - vec2.getZ()) * (vec1.getX() + vec2.getX())
				+ (vec2.getZ() - vec3.getZ()) * (vec2.getX() + vec3.getX())
				+ (vec3.getZ() - vec4.getZ()) * (vec3.getX() + vec4.getX())
				+ (vec4.getZ() - vec5.getZ()) * (vec4.getX() + vec5.getX())
				+ (vec5.getZ() - vec1.getZ()) * (vec5.getX() + vec1.getX());

		z = (vec1.getX() - vec2.getX()) * (vec1.getY() + vec2.getY())
				+ (vec2.getX() - vec3.getX()) * (vec2.getY() + vec3.getY())
				+ (vec3.getX() - vec4.getX()) * (vec3.getY() + vec4.getY())
				+ (vec4.getX() - vec5.getX()) * (vec4.getY() + vec5.getY())
				+ (vec5.getZ() - vec1.getZ()) * (vec5.getX() + vec1.getX());
		// now MyNormalise the result to have length 1
		MyNormalise();
	}

	/**
	 * Constructs a MyNormal vector by taking 6 co-planar points (not co-linear):
	 * Based on computing two vectors in the plane, then taking the vector cross
	 * product between these to compute the MyNormal; to get the sign right the
	 * vectors must be passed in anti-clockwise order.
	 * @author RIYAD K RAHMAN
	 * @param vec1
	 * @param vec2
	 * @param vec3
	 * @param vec4
	 * @param vec5
	 * @param vec6
	 */
	public MyNormal(Vector vec1, Vector vec2, Vector vec3, Vector vec4, Vector vec5, Vector vec6) {
		// we will hard-wire this - it is not pretty or clever, but easily understood.
		// in theory we should pass in an array of vectors and compute these terms in a
		// loop!
		x = (vec1.getY() - vec2.getY()) * (vec1.getZ() + vec2.getZ())
				+ (vec2.getY() - vec3.getY()) * (vec2.getZ() + vec3.getZ())
				+ (vec3.getY() - vec4.getY()) * (vec3.getZ() + vec4.getZ())
				+ (vec4.getY() - vec5.getY()) * (vec4.getZ() + vec5.getZ())
				+ (vec5.getY() - vec6.getY()) * (vec5.getZ() + vec6.getZ())
				+ (vec6.getY() - vec1.getY()) * (vec6.getZ() + vec1.getZ());

		y = (vec1.getZ() - vec2.getZ()) * (vec1.getX() + vec2.getX())
				+ (vec2.getZ() - vec3.getZ()) * (vec2.getX() + vec3.getX())
				+ (vec3.getZ() - vec4.getZ()) * (vec3.getX() + vec4.getX())
				+ (vec4.getZ() - vec5.getZ()) * (vec4.getX() + vec5.getX())
				+ (vec5.getZ() - vec6.getZ()) * (vec5.getX() + vec6.getX())
				+ (vec6.getZ() - vec1.getZ()) * (vec6.getX() + vec1.getX());

		z = (vec1.getX() - vec2.getX()) * (vec1.getY() + vec2.getY())
				+ (vec2.getX() - vec3.getX()) * (vec2.getY() + vec3.getY())
				+ (vec3.getX() - vec4.getX()) * (vec3.getY() + vec4.getY())
				+ (vec4.getX() - vec5.getX()) * (vec4.getY() + vec5.getY())
				+ (vec5.getZ() - vec6.getZ()) * (vec5.getX() + vec6.getX())
				+ (vec6.getZ() - vec1.getZ()) * (vec6.getX() + vec1.getX());
		// now MyNormalise the result to have length 1
		MyNormalise();
	}

	/**
	 * @return the x value of the MyNormal
	 */
	public final float getX() {
		return x;
	}

	/**
	 * @return the y value of the MyNormal
	 */
	public final float getY() {
		return y;
	}

	/**
	 * @return the z value of the MyNormal
	 */
	public final float getZ() {
		return z;
	}

	/**
	 * Submits this MyNormal to OpenGL using an immediate mode call
	 */
	public final void submit() {
		GL11.glNormal3f(x, y, z);
	}

	/**
	 * MyNormalises this MyNormal, so that the MyNormal is of unit length
	 */
	private void MyNormalise() {
		// compute the length of the MyNormal
		double length = Math.sqrt(x * x + y * y + z * z);

		// now divide each component by the length
		x /= length;
		y /= length;
		z /= length;
	}

	/** the x component of this MyNormal */
	private float x;
	/** the y component of this MyNormal */
	private float y;
	/** the z component of this MyNormal */
	private float z;

	
}
