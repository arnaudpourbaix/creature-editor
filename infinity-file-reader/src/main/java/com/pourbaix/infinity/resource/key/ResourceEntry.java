package com.pourbaix.infinity.resource.key;

import java.io.File;
import java.io.IOException;

public interface ResourceEntry {

	String getResourceName();

	String getExtension();

	File getOverrideFile();

	byte[] getResourceData() throws IOException;

	String getSearchString();

}
