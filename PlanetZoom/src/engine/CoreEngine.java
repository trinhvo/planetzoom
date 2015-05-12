package engine;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;
import static org.lwjgl.glfw.GLFW.*;
import input.Keyboard;
import input.Cursor;

import java.nio.ByteBuffer;

import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWvidmode;
import org.lwjgl.opengl.GLContext;

public class CoreEngine
{
    private final IGame game;
    public boolean running;
    public long windowHandle;
    
    private GLFWKeyCallback keyCallback;
    private GLFWCursorPosCallback cursorCallback;

    public Timer timer;
    
    public CoreEngine(IGame game)
    {
        this.game = game;
    }

    public void start()
    {
        running = true;

        init();

        while(running)
        {
            update();
            render();

            if(glfwWindowShouldClose(windowHandle) == GL_TRUE)
            {
                running = false;
            }
        }

        keyCallback.release();
        cursorCallback.release();
    }

    public void init()
    {
        if(glfwInit() != GL_TRUE)
        {
            System.err.println("can't initialize GLFW");
        }

        glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);
        // later necessary for OpenGL 3/4:
//      GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
//    	GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 2);
//    	GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
//    	GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
  
        windowHandle = glfwCreateWindow(800,600, "Stare into it device: " + windowHandle, NULL, NULL);

        if(windowHandle == NULL)
        {
            System.err.println("Window creation failed");
        }

        ByteBuffer vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        //System.out.println(GLFWvidmode.REFRESHRATE);
        
        glfwSetWindowPos(windowHandle, 100, 100);

        glfwMakeContextCurrent(windowHandle);
        glfwSwapInterval(1); //vSync

        glfwShowWindow(windowHandle);

        GLContext.createFromCurrent();

        glfwSetKeyCallback(windowHandle, keyCallback = new Keyboard());
        glfwSetCursorPosCallback(windowHandle, cursorCallback = new Cursor());

        game.init(windowHandle);
        
        timer = new Timer(); //not sure if best here
    }

    public void update()
    {
        glfwPollEvents();
        game.update();        
    }

    public void render()
    {
    	int deltaTime = timer.getDeltaTime();
    	//System.out.println(deltaTime);
    	//System.out.println(timer.getFPS());
    	game.render();
                
        glfwSwapBuffers(windowHandle);
    }
    
}
