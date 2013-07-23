package de.davboecki.multimodworld2.asm;

import static org.objectweb.asm.ClassWriter.COMPUTE_FRAMES;
import static org.objectweb.asm.ClassWriter.COMPUTE_MAXS;
import net.minecraft.launchwrapper.IClassTransformer;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

public class MainClassTransformer implements IClassTransformer {
	//TODO handle Obfuscation
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
				if(m.name.equals("handleFlying")) {
					MethodNode newMethod = new MethodNode(m.access, m.name, m.desc, m.signature, m.exceptions.toArray(new String[]{})) {
						private Boolean insertFirst = null;
						private Label labelForNextLineNumber = null;

						@Override
						public void visitFieldInsn(int opcode, String owner, String name, String desc) {
							super.visitFieldInsn(opcode, owner, name, desc);
							if(opcode == Opcodes.PUTFIELD && name.equals("hasMoved") && owner.equals("net/minecraft/network/NetServerHandler") && desc.equals("Z")) {
								if(insertFirst == null) {
									insertFirst = true;
								}
							}
						}

						@Override
						public void visitLabel(Label label) {
							if(insertFirst != null && insertFirst) {
								insertFirst = false;
								super.visitLabel(label);
								visitFrame(Opcodes.F_APPEND,1, new Object[] {"net/minecraft/world/WorldServer"}, 0, null);
								visitVarInsn(Opcodes.ALOAD, 0);
								visitFieldInsn(Opcodes.GETFIELD, "net/minecraft/network/NetServerHandler", "playerEntity", "Lnet/minecraft/entity/player/EntityPlayerMP;");
								visitVarInsn(Opcodes.ALOAD, 1);
								visitVarInsn(Opcodes.ALOAD, 0);
								visitFieldInsn(Opcodes.GETFIELD, "net/minecraft/network/NetServerHandler", "hasMoved", "Z");
								visitMethodInsn(Opcodes.INVOKESTATIC, "de/davboecki/multimodworld2/asm/MultiModWorldHooks", "handlePlayerMove", "(Lnet/minecraft/entity/player/EntityPlayerMP;Lnet/minecraft/network/packet/Packet10Flying;Z)Z");
								Label l9 = new Label();
								visitJumpInsn(Opcodes.IFEQ, l9);
								visitInsn(Opcodes.RETURN);
								super.visitLabel(l9);
								labelForNextLineNumber = l9;
							} else {
								super.visitLabel(label);
							}
						}

						@Override
						public void visitLineNumber(int line, Label start) {
							if(labelForNextLineNumber == null) {
								super.visitLineNumber(line, start);
							} else {
								super.visitLineNumber(line, labelForNextLineNumber);
								labelForNextLineNumber = null;
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
		if(transformedName.endsWith("net.minecraft.world.World")) {
			ClassReader reader = new ClassReader(bytes);
			ClassNode node = new ClassNode();
			reader.accept(node, 0);
			for(MethodNode m:node.methods) {
				if((m.name.equals("setBlock") && m.desc.equals("(IIIIII)Z")) || m.name.equals("setBlockMetadataWithNotify")) {
					MethodNode newMethod = new MethodNode(m.access, m.name, m.desc, m.signature, m.exceptions.toArray(new String[]{})) {
						@Override
						public void visitCode() {
							visitLabel(new Label());
							visitVarInsn(Opcodes.ILOAD, 1);
							visitVarInsn(Opcodes.ILOAD, 2);
							visitVarInsn(Opcodes.ILOAD, 3);
							visitVarInsn(Opcodes.ILOAD, 4);
							visitVarInsn(Opcodes.ILOAD, 5);
							visitVarInsn(Opcodes.ALOAD, 0);
							visitVarInsn(Opcodes.ALOAD, 0);
							visitFieldInsn(Opcodes.GETFIELD, "net/minecraft/world/World", "worldInfo", "Lnet/minecraft/world/storage/WorldInfo;");
							visitMethodInsn(Opcodes.INVOKESTATIC, "de/davboecki/multimodworld2/asm/MultiModWorldHooks", "setBlock", "(IIIIILnet/minecraft/world/World;Lnet/minecraft/world/storage/WorldInfo;)Z");
							Label l1 = new Label();
							visitJumpInsn(Opcodes.IFNE, l1);
							visitInsn(Opcodes.ICONST_0);
							visitInsn(Opcodes.IRETURN);
							visitLabel(l1);
							super.visitCode();
						}
					};
					m.accept(newMethod);
					node.methods.set(node.methods.indexOf(m), newMethod);
				} else if(m.name.equals("setBlockTileEntity")) {
					MethodNode newMethod = new MethodNode(m.access, m.name, m.desc, m.signature, m.exceptions.toArray(new String[]{})) {
						@Override
						public void visitCode() {
							visitLabel(new Label());
							visitVarInsn(Opcodes.ILOAD, 1);
							visitVarInsn(Opcodes.ILOAD, 2);
							visitVarInsn(Opcodes.ILOAD, 3);
							visitVarInsn(Opcodes.ALOAD, 4);
							visitVarInsn(Opcodes.ALOAD, 0);
							visitVarInsn(Opcodes.ALOAD, 0);
							visitFieldInsn(Opcodes.GETFIELD, "net/minecraft/world/World", "worldInfo", "Lnet/minecraft/world/storage/WorldInfo;");
							visitMethodInsn(Opcodes.INVOKESTATIC, "de/davboecki/multimodworld2/asm/MultiModWorldHooks", "setBlockTileEntity", "(IIILnet/minecraft/tileentity/TileEntity;Lnet/minecraft/world/World;Lnet/minecraft/world/storage/WorldInfo;)Z");
							Label l1 = new Label();
							visitJumpInsn(Opcodes.IFNE, l1);
							visitInsn(Opcodes.RETURN);
							visitLabel(l1);
							super.visitCode();
						}
					};
					m.accept(newMethod);
					node.methods.set(node.methods.indexOf(m), newMethod);
				} else if(m.name.equals("spawnEntityInWorld")) {
					MethodNode newMethod = new MethodNode(m.access, m.name, m.desc, m.signature, m.exceptions.toArray(new String[]{})) {
						@Override
						public void visitCode() {
							visitLabel(new Label());
							visitVarInsn(Opcodes.ALOAD, 1);
							visitVarInsn(Opcodes.ALOAD, 0);
							visitVarInsn(Opcodes.ALOAD, 0);
							visitFieldInsn(Opcodes.GETFIELD, "net/minecraft/world/World", "worldInfo", "Lnet/minecraft/world/storage/WorldInfo;");
							visitMethodInsn(Opcodes.INVOKESTATIC, "de/davboecki/multimodworld2/asm/MultiModWorldHooks", "spawnEntityInWorld", "(Lnet/minecraft/entity/Entity;Lnet/minecraft/world/World;Lnet/minecraft/world/storage/WorldInfo;)Z");
							Label l1 = new Label();
							visitJumpInsn(Opcodes.IFNE, l1);
							visitInsn(Opcodes.ICONST_0);
							visitInsn(Opcodes.IRETURN);
							visitLabel(l1);
							super.visitCode();
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
		if(transformedName.endsWith("net.minecraft.network.packet.Packet1Login")) {
			ClassReader reader = new ClassReader(bytes);
			ClassNode node = new ClassNode();
			reader.accept(node, 0);
			node.interfaces.add("de/davboecki/multimodworld2/asm/interfaces/ISetVanillaPacket");
			MethodVisitor mv = node.visitMethod(Opcodes.ACC_PUBLIC, "setVanillaPacket", "()V", null, null);
			{
				mv.visitCode();
				Label l0 = new Label();
				mv.visitLabel(l0);
				mv.visitVarInsn(Opcodes.ALOAD, 0);
				mv.visitInsn(Opcodes.ICONST_1);
				mv.visitFieldInsn(Opcodes.PUTFIELD, "net/minecraft/network/packet/Packet1Login", "vanillaCompatible", "Z");
				Label l1 = new Label();
				mv.visitLabel(l1);
				mv.visitInsn(Opcodes.RETURN);
				Label l2 = new Label();
				mv.visitLabel(l2);
				mv.visitLocalVariable("this", "Lnet/minecraft/network/packet/Packet1Login;", null, l0, l2, 0);
				mv.visitMaxs(2, 1);
				mv.visitEnd();
			}
			ClassWriter writer = new ClassWriter(COMPUTE_MAXS | COMPUTE_FRAMES);
			node.accept(writer);
			return writer.toByteArray();
		}
		return bytes;
	}
}