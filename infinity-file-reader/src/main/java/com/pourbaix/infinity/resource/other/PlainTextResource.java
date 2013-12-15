package com.pourbaix.infinity.resource.other;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import com.pourbaix.infinity.resource.TextResource;
import com.pourbaix.infinity.resource.key.ResourceEntry;
import com.pourbaix.infinity.util.Decryptor;

public final class PlainTextResource implements TextResource, Closeable {
	private final ResourceEntry entry;
	private final String text;

	public PlainTextResource(ResourceEntry entry) throws Exception {
		this.entry = entry;
		byte data[] = entry.getResourceData();
		if (data != null && data.length > 1 && data[0] == -1)
			text = Decryptor.decrypt(data, 2, data.length);
		else
			text = new String(data);
	}

	@Override
	public void close() {
	}

	@Override
	public ResourceEntry getResourceEntry() {
		return entry;
	}

	@Override
	public String getText() {
		return text;
	}

	public List<String> extract2DAHeaders() {
		StringTokenizer st = new StringTokenizer(getText(), "\n");
		st.nextToken();
		st.nextToken();
		String header = st.nextToken();
		st = new StringTokenizer(header);
		List<String> strings = new ArrayList<String>();
		while (st.hasMoreTokens())
			strings.add(st.nextToken().toUpperCase());
		return strings;
	}
}
