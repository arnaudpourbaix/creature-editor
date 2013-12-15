package com.pourbaix.infinity.resource.key;

import java.io.File;

public interface ResourceEntry {

	String getResourceName();

	String getExtension();

	File getOverrideFile();

	byte[] getResourceData() throws Exception;

	String getSearchString();

}
