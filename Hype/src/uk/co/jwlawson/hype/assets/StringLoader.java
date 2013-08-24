package uk.co.jwlawson.hype.assets;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;

public class StringLoader extends AsynchronousAssetLoader<String, StringLoader.StringParameter> {

	public StringLoader(FileHandleResolver resolver) {
		super(resolver);
	}

	String string;

	@Override
	public void loadAsync(AssetManager manager, String fileName, FileHandle file,
			StringParameter parameter) {
		string = null;
		string = file.readString();
	}

	@Override
	public String loadSync(AssetManager manager, String fileName, FileHandle file,
			StringParameter parameter) {
		return string;
	}

	@Override
	public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file,
			StringParameter parameter) {
		return null;
	}

	static public class StringParameter extends AssetLoaderParameters<String> {
	}
}
