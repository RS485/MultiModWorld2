package de.davboecki.multimodworld2.asm;

import static org.objectweb.asm.ClassWriter.COMPUTE_FRAMES;
import static org.objectweb.asm.ClassWriter.COMPUTE_MAXS;
import net.minecraft.launchwrapper.IClassTransformer;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

public class MainClassTransformer implements IClassTransformer {

	@Override
	public byte[] transform(String name, String transformedName, byte[] bytes) {
		if(transformedName.endsWith("net.minecraft.network.NetServerHandler")) {
			ClassReader reader = new ClassReader(bytes);
			ClassNode node = new ClassNode();
			reader.accept(node, 0);
			for(MethodNode m:node.methods) {
				if(m.name.equals("sendPacketToPlayer")) {
					MethodNode newMethod = new MethodNode(m.access, m.name, m.desc, m.signature, m.exceptions.toArray(new String[]{})) {
						@Override
						public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
							super.visitTryCatchBlock(start, end, handler, type);
							Label l3 = new Label();
							visitLabel(l3);
							visitVarInsn(Opcodes.ALOAD, 1);
							visitMethodInsn(Opcodes.INVOKESTATIC, "de/davboecki/multimodworld2/asm/MultiModWorldHooks", "handlePacket", "(Lnet/minecraft/network/packet/Packet;)Z");
							Label l4 = new Label();
							visitJumpInsn(Opcodes.IFNE, l4);
							visitInsn(Opcodes.RETURN);
							visitLabel(l4);
						}
					};
					m.accept(newMethod);
					node.methods.set(node.methods.indexOf(m), newMethod);
				}
			}
			ClassWriter writer = new ClassWriter(COMPUTE_MAXS | COMPUTE_FRAMES);
			node.accept(writer);
			return writer.toByteArray();
		}
		if(transformedName.endsWith("cpw.mods.fml.common.network.FMLNetworkHandler")) {
			ClassReader reader = new ClassReader(bytes);
			ClassNode node = new ClassNode();
			reader.accept(node, 0);
			for(MethodNode m:node.methods) {
				if(m.name.equals("handleClientConnection")) {
					MethodNode newMethod = new MethodNode(m.access, m.name, m.desc, m.signature, m.exceptions.toArray(new String[]{})) {
						@Override
						public void visitCode() {
							Label l0 = new Label();
							visitLabel(l0);
							visitLineNumber(126, l0);
							visitVarInsn(Opcodes.ALOAD, 0);
							visitFieldInsn(Opcodes.GETFIELD, "cpw/mods/fml/common/network/FMLNetworkHandler", "loginStates", "Ljava/util/Map;");
							visitVarInsn(Opcodes.ALOAD, 1);
							visitVarInsn(Opcodes.ALOAD, 4);
							visitMethodInsn(Opcodes.INVOKESTATIC, "de/davboecki/multimodworld2/asm/MultiModWorldHooks", "handleConnection", "(Ljava/util/Map;Lnet/minecraft/network/NetLoginHandler;Ljava/lang/String;)V");
							super.visitCode();
						}
						
						private boolean next = false;
						
						@Override
						public void visitMethodInsn(int opcode, String owner, String name, String desc) {
							super.visitMethodInsn(opcode, owner, name, desc);
							if(name.equals("func_72531_a")) {
								next = true;
							}
						}

						@Override
						public void visitLineNumber(int line, Label start) {
							if(!next) {
								super.visitLineNumber(line, start);
							} else {
								next = false;
								visitVarInsn(Opcodes.ALOAD, 0);
								visitFieldInsn(Opcodes.GETFIELD, "cpw/mods/fml/common/network/FMLNetworkHandler", "loginStates", "Ljava/util/Map;");
								visitVarInsn(Opcodes.ALOAD, 1);
								visitVarInsn(Opcodes.ALOAD, 4);
								visitMethodInsn(Opcodes.INVOKESTATIC, "de/davboecki/multimodworld2/asm/MultiModWorldHooks", "sendModRequest", "(Ljava/util/Map;Lnet/minecraft/network/NetLoginHandler;Ljava/lang/String;)Z");
								Label l23 = new Label();
								visitJumpInsn(Opcodes.IFNE, l23);
								visitInsn(Opcodes.RETURN);
								visitLabel(l23);
								//visitLineNumber(165, l23);
								visitFrame(Opcodes.F_SAME, 0, null, 0, null);
							}
						}
						
					};
					m.accept(newMethod);
					node.methods.set(node.methods.indexOf(m), newMethod);
				}
			}
			ClassWriter writer = new ClassWriter(COMPUTE_MAXS | COMPUTE_FRAMES);
			node.accept(writer);
			return writer.toByteArray();
		}
		return bytes;
	}
}
