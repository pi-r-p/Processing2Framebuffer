package fr.couincouin;

import java.util.HashMap;
import java.util.Map;

import io.warp10.warp.sdk.WarpScriptExtension;

public class processingToFramebufferExtension extends WarpScriptExtension {
  
  private static final Map<String,Object> functions;
  
  static {
    functions = new HashMap<String,Object>();
    functions.put("PtoFrameBuffer", new PtoFramebuffer("PtoFrameBuffer"));
  }
  
  @Override
  public Map<String, Object> getFunctions() {
    return functions;
  }
}
