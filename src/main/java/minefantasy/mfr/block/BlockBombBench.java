package minefantasy.mfr.block;

import minefantasy.mfr.init.KnowledgeListMFR;
import minefantasy.mfr.init.MineFantasyTabs;
import minefantasy.mfr.mechanics.knowledge.ResearchLogic;
import minefantasy.mfr.tile.TileEntityBombBench;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class BlockBombBench extends BlockTileEntity<TileEntityBombBench> {

	public BlockBombBench() {
		super(Material.WOOD);

		setRegistryName("bomb_bench");
		setUnlocalizedName("bomb_bench");
		this.setSoundType(SoundType.STONE);
		this.setHardness(5F);
		this.setResistance(2F);
		this.setLightOpacity(0);
		this.setCreativeTab(MineFantasyTabs.tabUtil);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this);
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileEntityBombBench();
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	/**
	 * Called when the block is placed in the world.
	 */
	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase user, ItemStack stack) {
		world.setBlockState(pos, state, 2);
	}

	/**
	 * Called upon block activation (right click on the block.)
	 */
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (ResearchLogic.hasInfoUnlocked(player, KnowledgeListMFR.bombs)) {
			if (world.isRemote)
				player.sendMessage(new TextComponentString(I18n.format("knowledge.unknownUse")));
			return false;
		}
		TileEntityBombBench tile = (TileEntityBombBench) getTile(world, pos);
		if (tile != null && !world.isSideSolid(pos.add(0, 1, 0), EnumFacing.DOWN)) {
			if (facing != EnumFacing.UP || !tile.tryCraft(player, false) && !world.isRemote) {
				tile.openGUI(world, player);
			}
		}
		return true;
	}

	@Override
	public void onBlockClicked(World world, BlockPos pos, EntityPlayer user) {
		if (ResearchLogic.hasInfoUnlocked(user, KnowledgeListMFR.bombs)) {
			TileEntityBombBench tile = (TileEntityBombBench) getTile(world, pos);
			if (tile != null) {
				tile.tryCraft(user, false);
			}
		}
	}
}