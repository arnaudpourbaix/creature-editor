package com.pourbaix.infinity.resource;

import java.io.IOException;
import java.io.OutputStream;

public interface Writeable
{
  void write(OutputStream os) throws IOException;
}

