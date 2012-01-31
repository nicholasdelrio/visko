package trustlab.visko.execution.paths;

import java.util.*;

public class FormatPaths extends Vector<FormatPath> {

	public boolean add(FormatPath path) {
		if (!isExistingPath(path)) {
			return super.add(path);
		}

		return false;
	}

	private boolean isExistingPath(FormatPath path) {
		for (Vector<String> aPath : this) {
			if (aPath.toString().equals(path.toString()))
				return true;
		}
		return false;
	}
}
