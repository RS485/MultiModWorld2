package de.davboecki.multimodworld2.asm;

import java.util.Map;

import cpw.mods.fml.relauncher.IFMLCallHook;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;

@TransformerExclusions({"de.davboecki.multimodworld2.asm.MainClassTransformer"})
public class MultiModWorldLoader implements IFMLLoadingPlugin, IFMLCallHook {

	@Override
	@Deprecated
	public String[] getLibraryRequestClass() {
		return null;
	}

	@Override
	public String[] getASMTransformerClass() {
		return new String[]{"de.davboecki.multimodworld2.asm.MainClassTransformer"};
	}

	@Override
	public String getModContainerClass() {
		return null; //TODO
	}

	@Override
	public String getSetupClass() {
		return null;//"de.davboecki.multimodworld2.asm.MultiModWorldLoader";
	}

	@Override
	public void injectData(Map<String, Object> data) {}

	@Override
	public Void call() throws Exception {
		return null;
	}
}
