package fr.couincouin;

import io.warp10.script.NamedWarpScriptFunction;
import io.warp10.script.WarpScriptException;
import io.warp10.script.WarpScriptStack;
import io.warp10.script.WarpScriptStackFunction;
import processing.core.PGraphics;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

public class PtoFramebuffer extends NamedWarpScriptFunction implements WarpScriptStackFunction {

  public PtoFramebuffer(String name) {
    super(name);
  }

  @Override
  public Object apply(WarpScriptStack stack) throws WarpScriptException {

    if (stack.depth() < 2) {
      throw new WarpScriptException(getName() + " expects a PGRAPHICS and a STRING on top of the stack.");
    }
    Object fbpath = stack.pop();
    Object pimage = stack.pop();

    if (pimage instanceof PGraphics && fbpath instanceof String) {
      PGraphics pg = (PGraphics) pimage;
      pg.loadPixels();
      ByteBuffer bytes = ByteBuffer.allocate(pg.width * pg.height * 4); // 4 bytes per pixel
      for (int pixel: pg.pixels) {
        bytes.put((byte) (pixel & 0xFF)); //blue
        bytes.put((byte) ((pixel & 0xFF00) >> 8)); //green
        bytes.put((byte) ((pixel & 0xFF0000) >> 16)); //red
        bytes.put((byte) 0);
      }
      File file = new File((String) fbpath);
      try {
        Files.write(file.toPath(), bytes.array(), StandardOpenOption.CREATE_NEW);
      } catch (IOException e) {
        throw new WarpScriptException("Cannot write file " + file.toString());
      }
    } else {
      throw new WarpScriptException(getName() + " expects a STRING to specify the frame buffer path on top of the stack.");
    }
    return stack;
  }
}
