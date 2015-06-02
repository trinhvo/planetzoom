package engine;

import geometry.Vertex2D;

import java.util.ArrayList;

import Peter.TextureUsingPNGDecoder;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class Text2D extends GameObject2D
{
	
	private static final int ROWCOUNT_STANDARD = 16;
	private static final int COLUMNCOUNT_STANDARD = 16;
	/**
	 * 
	 * @param text text to be rendered
	 * @param font name of the font bitmap (located in res.textures.fonts)
	 * @param x	x_cooridnate
	 * @param y y_coordinate
	 * @param size size of a character in the given bitmap
	 */
	public Text2D(String text, String font, int x, int y, int size)
	{
		this(text, font, x, y, size, ROWCOUNT_STANDARD, COLUMNCOUNT_STANDARD);
	}
	
	/**
	 * 
	 * @param text text to be rendered
	 * @param font name of the font bitmap (located in res.textures.fonts)
	 * @param x	x_cooridnate
	 * @param y y_coordinate
	 * @param size size of a character-cell in the given bitmap
	 * @param number of character rows in the given bitmap
	 * @param number of character columns in the given bitmap
	 */
	public Text2D(String text, String font, int x, int y, int size, int rowCount, int colCount)
	{
		super();
		int index = 0;
		char currentChar;
		float uv_x, uv_y;
		
		float cellHeight = 1.0f / colCount;
		float cellWidth = 1.0f / rowCount;
		
		int row, column;
		
		indices = new int[text.length() * 6];
		ArrayList<Vector2f> positions = new ArrayList<>();;
		ArrayList<Vector2f> uvs = new ArrayList<>();
		
        Vector3f normal = new Vector3f(0, 0, 1);
        
		for(int i = 0; i < text.length(); i++)
		{
			currentChar = text.charAt(i);
			row = (currentChar % rowCount);
			column = (currentChar / colCount);
			
			uv_x =  row / (float) rowCount;
			uv_y =  column / (float) colCount;
			
			if(currentChar == '\n')
			{
				y += size;
				x = 0;
				continue;
			}
				 
			positions.add(new Vector2f(x, (y + size))); 		 	// top left			index
			positions.add(new Vector2f(x + size, (y + size))); 		// top right		index + 1
			positions.add(new Vector2f(x, y)); 			 		// bottom left		index + 2
			positions.add(new Vector2f(x + size, y)); 		 		// bottom right		index + 3
				
			x += size;
			
			uvs.add(new Vector2f(uv_x, uv_y + cellHeight)); 	  			// top left			index 
			uvs.add(new Vector2f(uv_x + cellWidth, uv_y + cellHeight));  	// top right		index + 1
			uvs.add(new Vector2f(uv_x, uv_y));			  					// bottom left		index + 2
			uvs.add(new Vector2f(uv_x + cellWidth, uv_y));		  			// bottom right		index + 3

	
			vertices.add(new Vertex2D(positions.get(index), uvs.get(index), normal));
			vertices.add(new Vertex2D(positions.get(index + 1), uvs.get(index + 1), normal));
			vertices.add(new Vertex2D(positions.get(index + 2), uvs.get(index + 2), normal));
			vertices.add(new Vertex2D(positions.get(index + 3), uvs.get(index + 3), normal));
			
			//First triangle
			indices[(i * 6) + 0] = index + 2;
			indices[(i * 6) + 1] = index;
			indices[(i * 6) + 2] = index + 3;
			
			//Second triangle
			indices[(i * 6) + 3] = index;
			indices[(i * 6) + 4] = index + 1;
			indices[(i * 6) + 5] = index + 3;
			
			index += 4;
		}
		
		setTexture(new TextureUsingPNGDecoder("src/res/textures/fonts/" + font));
		createVAO();
	}
}