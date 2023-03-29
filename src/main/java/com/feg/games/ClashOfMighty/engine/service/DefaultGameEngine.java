package com.feg.games.ClashOfMighty.engine.service;

import com.feg.games.ClashOfMighty.engine.bonus.RngBonusContext;
import com.feg.games.ClashOfMighty.engine.bonus.RngGameBonus;
import com.feg.games.ClashOfMighty.engine.config.RandomPoolConfig;
import com.feg.games.ClashOfMighty.engine.config.RngGameConfiguration;
import com.feg.games.ClashOfMighty.engine.jackson.EngineModule;
import com.feg.games.ClashOfMighty.engine.model.CustomReelsSymbolGrid;
import com.feg.games.ClashOfMighty.engine.model.RNG;
import com.feg.games.ClashOfMighty.engine.model.dto.RngGameEngineRequest;
import com.feg.games.ClashOfMighty.engine.model.utils.FeatureUtils;
import com.feg.games.ClashOfMighty.engine.symbols.RngSymbol;
import com.feg.games.ClashOfMighty.ext.api.bonus.BonusContext;
import com.feg.games.ClashOfMighty.ext.api.exception.GameEngineException;
import com.feg.games.ClashOfMighty.ext.api.model.*;
import com.feg.games.ClashOfMighty.ext.api.module.GameEngineModule;
import com.feg.games.ClashOfMighty.ext.api.symbol.Symbol;
import com.feg.games.ClashOfMighty.ext.slots.api.dto.SlotsGameEngineResponse;
import com.feg.games.ClashOfMighty.ext.slots.api.model.SlotsGamePlayState;
import com.feg.games.ClashOfMighty.ext.slots.config.SlotsFreeGameConfiguration;
import com.feg.games.ClashOfMighty.ext.slots.model.PayWay;
import com.feg.games.ClashOfMighty.ext.slots.model.SlotsBonusContext;
import com.feg.games.ClashOfMighty.ext.slots.model.SlotsPay;
import com.feg.games.ClashOfMighty.ext.slots.model.SlotsSpinGameActivity;
import com.feg.games.ClashOfMighty.ext.slots.pays.PayStep;
import com.feg.games.ClashOfMighty.ext.slots.reels.*;
import com.feg.games.ClashOfMighty.ext.slots.bonus.FreeSpinsContext;
import com.feg.games.ClashOfMighty.ext.slots.bonus.handler.CashOrFreeSpinsBonusOptionsHandler;
import com.feg.games.ClashOfMighty.ext.slots.bonus.handler.FreeSpinBonusHandler;
import com.feg.games.ClashOfMighty.ext.slots.bonus.handler.ScatterBonusHandler;
import com.feg.games.ClashOfMighty.ext.slots.bonus.handler.ScatterPayHandler;
import com.feg.games.ClashOfMighty.ext.slots.handlers.DefaultClusterPaysHandler;
import com.feg.games.ClashOfMighty.ext.slots.handlers.DefaultPayStepWinningsHandler;
import com.feg.games.ClashOfMighty.ext.slots.handlers.DefaultPayWayHandler;
import com.feg.games.ClashOfMighty.ext.slots.utils.PickByWeightage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.monitorjbl.json.JsonView;
import com.monitorjbl.json.JsonViewModule;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import java.io.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.monitorjbl.json.Match.match;

@Service
@Data
@Slf4j
@NoArgsConstructor
public class DefaultGameEngine {
        private RngGameConfiguration gc  = null;
        private RngGameConfiguration nonGc  = null;
        private String gameInitializeData = null;
        private final Map<String, String> gameConfigurationJsonMap = new HashMap<>();
        private  List<String> stackReelSettings = new ArrayList<>();

        @Autowired
        private ResourceLoader resourceLoader;
        @Autowired
        private ObjectMapper objectMapper;
        @Autowired
        private DefaultSlotReelSpinner reelSpinner;
        @Autowired
        private CashOrFreeSpinsBonusOptionsHandler<RngSymbol, RngGameBonus> cashOrFreeSpinsBonusOptionsHandler;
        @Autowired
        private FreeSpinBonusHandler<RngSymbol, RngGameBonus> freeSpinBonusHandler;
        @Autowired
        private ScatterBonusHandler<RngSymbol> scatterBonusHandler;
        @Autowired
        private ScatterPayHandler scatterPayHandler;
        @Autowired
        private DefaultPayWayHandler payWaysHandler;
        @Autowired
        private DefaultPayStepWinningsHandler payStepWinningsHandler;
        @Autowired
        private DefaultClusterPaysHandler<RngSymbol> clusterPaysHandler;
        @Autowired
        private DefaultSlotReelLayoutSelector layoutSelector;
        @Autowired
        private PickByWeightage pickByWeightage;
        @Autowired
        RNG rng;

        @PostConstruct
        public void init() throws IOException {
            log.info(">>>> Received initialize request for new Game Play");

            this.objectMapper = objectMapper.copy().registerModule(new JsonViewModule());

            InputStreamReader is;
            is = new InputStreamReader(resourceLoader.getResource("classpath:" + "rtp-96" + ".json").getInputStream());
            try (BufferedReader reader = new BufferedReader(
                    is)) {
                String json = reader.lines()
                        .collect(Collectors.joining("\n"));
                this.gc = objectMapper
                        .readValue(json,
                                RngGameConfiguration.class);
            }
            log.info(">>>> RngGameConfiguration data read and stored and gc");
        }

        public RngGameConfiguration getGameConfigurationJson() {
            RngGameConfiguration gc = this.gc;
        try{
            this.nonGc = objectMapper.readValue(objectMapper.writeValueAsString(JsonView.with(gc)
                    .onClass(RngGameConfiguration.class, match()
                            .exclude("bonusGameConfiguration",
                                    "buyFeatureBetMultiplier",
                                    "buyFeatureReels","randomConfiguration"
                            ))), RngGameConfiguration.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        log.info(">>>> return gc to response "+this.nonGc);
        return nonGc;
    }

        public GamePlay startGame() {
            log.info(">>>> Start game method execution started ");
            GamePlay gamePlay = new GamePlay(GameType.SLOTS);
            log.info(">>>> GameType set to "+gamePlay.getGameType());
            SlotsGamePlayState slotsGamePlay = new SlotsGamePlayState();
            slotsGamePlay.setGameStatus(GameStatus.INPROGRESS);
            log.info(">>>> GameStatus set to "+slotsGamePlay.getGameStatus());
            slotsGamePlay.getBonusAwarded().add(RngGameBonus.NONE);
            slotsGamePlay.setGameType(gamePlay.getGameType());
            gamePlay.setGamePlayState(slotsGamePlay);
            log.info(">>>> gamePlay return from Start Method "+gamePlay);
            return gamePlay;
        }

        public SlotsGameEngineResponse validate(RngGameEngineRequest gameEngineRequest, SlotsGameEngineResponse response) throws GameEngineException {
            GamePlay slotsGamePlay = response.getGamePlay();
            RngGameConfiguration gc = this.gc;
            int playingLines = gameEngineRequest.getNoOfLines();
            if (gameEngineRequest.getNoOfLines() == null || gameEngineRequest.getNoOfLines() == 0 ||
                    gc.getStakeSettings() == null || gc.getStakeSettings().getMinMaxLines() == null) {
                throw new GameEngineException("Invalid playLine request.");
            }
            int minPayLines = gc.getStakeSettings().getMinMaxLines()[0];
            int maxPayLines = gc.getStakeSettings().getMinMaxLines()[1];
            if (playingLines < minPayLines || playingLines > maxPayLines) {
                throw new GameEngineException("Invalid playLine request.");
            }
            if (gameEngineRequest.getStakePerLine() == null
                    || gameEngineRequest.getStakePerLine().compareTo(BigDecimal.ZERO) <= 0)
                throw new GameEngineException("Invalid Stake.");
            SlotsGamePlayState slotsGamePlayState = (SlotsGamePlayState) slotsGamePlay.getGamePlayState();
            slotsGamePlayState.setNoOfLines(gameEngineRequest.getNoOfLines());
            slotsGamePlayState.setStakePerLine(gameEngineRequest.getStakePerLine());
            BigDecimal totalStake = getTotalStake(gameEngineRequest.getStakePerLine(), gameEngineRequest.getNoOfLines());
            boolean buyFeature = (gameEngineRequest.isBuyFeature()
                    && (slotsGamePlayState.getLastBonusAwarded().equals(RngGameBonus.NONE)));
            BigDecimal buyStake = null;
            if(buyFeature){
                buyStake = gc.getBuyFeatureBetMultiplier();
                buyStake = GamePlay.scaledValue(buyStake.multiply(gameEngineRequest.getStakePerLine()).multiply(BigDecimal.valueOf(gameEngineRequest.getNoOfLines())));
            }
            slotsGamePlayState.setTotalStake(buyStake!=null?buyStake:totalStake);
            slotsGamePlay.setGamePlayState(slotsGamePlayState);
            response.setTotalBet(slotsGamePlayState.getTotalStake());
            response.setGamePlay(slotsGamePlay);
            return response;
        }

        public SlotsGameEngineResponse play(RngGameEngineRequest slotsGameEngineRequest, SlotsGameEngineResponse response) throws GameEngineException {
            GamePlay gamePlay = response.getGamePlay();
            SlotsGamePlayState slotsGamePlayState      = (SlotsGamePlayState) gamePlay.getGamePlayState();

            if (slotsGamePlayState.getLastBonusAwarded()    == null
                    || slotsGamePlayState.getGameStatus()   == GameStatus.COMPLETED)
                throw new GameEngineException("Invalid Game State");
            boolean  buyFeature   = ( slotsGameEngineRequest.isBuyFeature() && slotsGamePlayState.getLastBonusAwarded().equals(RngGameBonus.NONE));
            final RngGameBonus gameBonus   = (RngGameBonus) slotsGamePlayState.getLastBonusAwarded();
            SlotsSpinGameActivity slotSpinActivity;
            if (buyFeature)
                slotSpinActivity    = processBuyFeature(gamePlay, RngGameBonus.BUY_FEATURE);
            else if (gameBonus.equals(RngGameBonus.NONE) || gameBonus.equals(RngGameBonus.FREE_SPINS) || gameBonus.equals(RngGameBonus.BUY_FEATURE_FREE_SPINS))
                slotSpinActivity    = processBaseSpin(gamePlay, gameBonus);
            else if (gameBonus.equals(RngGameBonus.NONE_RESPIN)||gameBonus.equals(RngGameBonus.FREE_RESPIN)||
                    gameBonus.equals(RngGameBonus.BUY_FEATURE_RESPIN))
                slotSpinActivity    = processReSpin(gamePlay, gameBonus);
            else throw new GameEngineException("Invalid Game process");
            response.setGameActivity(slotSpinActivity);
            response.setGamePlay(gamePlay);
            return response;
        }


        public SlotsGameEngineResponse processWinnings(RngGameEngineRequest request, SlotsGameEngineResponse response) {
            GamePlay gamePlay = response.getGamePlay();
            GameActivity gameActivity = response.getGameActivity();
            SlotsGamePlayState slotsGamePlayState = (SlotsGamePlayState) gamePlay.getGamePlayState();
            response.setTotalBet(slotsGamePlayState.getTotalStake());
            response.setGamePlay(gamePlay);
            SlotsSpinGameActivity spinGameActivity = (SlotsSpinGameActivity) gameActivity;
            BigDecimal spinWinnings = spinGameActivity.getPaySteps()
                    .stream()
                    .map(payStep -> payStepWinningsHandler
                            .payStepWinnings(slotsGamePlayState.getTotalStake(),
                                    slotsGamePlayState.getTotalStake(),
                                    slotsGamePlayState.getStakePerLine(),
                                    slotsGamePlayState.getNoOfLines(), payStep))
                    .reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
            spinGameActivity.setWinnings(spinWinnings);
            if(spinGameActivity.getTotalWinnings()!=null)
                spinGameActivity.setTotalWinnings(spinGameActivity.getTotalWinnings().add(spinWinnings));
            response.setSpinPayout(spinWinnings);
            slotsGamePlayState.setTotalWinnings(slotsGamePlayState.getTotalWinnings().add(spinWinnings));
            response.setTotalPayout(slotsGamePlayState.getTotalWinnings());
            response.setGameActivity(spinGameActivity);
            return response;
        }

        private SlotsSpinGameActivity processBuyFeature(GamePlay gamePlay, RngGameBonus gameBonus) {
        SlotsGamePlayState slotsGamePlayState          = (SlotsGamePlayState) gamePlay.getGamePlayState();
        RngGameConfiguration gc                          = this.gc;

        RandomPoolConfig randomPoolConfig = gc.getRandomConfiguration().get(gameBonus);
        int randomsRequired = randomPoolConfig.getNumbers();
        HashMap<String, Integer> randomList = randomPoolConfig.getRandomList();
        Map<String, Integer> randomNumbersWithKey = rng.randomProvider(randomList);
        if(randomNumbersWithKey.size()!=randomsRequired) throw new GameEngineException("required random numbers not provided");

        RngBonusContext previousContext     = (RngBonusContext) slotsGamePlayState.getBonusContext();
        ReelLayout<RngSymbol, RngGameBonus> reelLayout = new ReelLayout<>();
        List<SlotReel<RngSymbol>> reels = new ArrayList<>();
        int pick = randomNumbersWithKey.get("reelLayout");
        List<Integer> reelComboPattern =pickByWeightage.pick(gc.getBuyFeatureReelConfiguration(),pick).getValue();
        List<RngSymbol> reel0 =gc.getBuyFeatureReels().get(reelComboPattern.get(0)).getReel1();
        List<RngSymbol> reel1 =gc.getBuyFeatureReels().get(reelComboPattern.get(1)).getReel2();
        List<RngSymbol> reel2 =gc.getBuyFeatureReels().get(reelComboPattern.get(2)).getReel3();
        List<RngSymbol> reel3 =new ArrayList<>(Arrays.asList(RngSymbol.Blank, RngSymbol.Blank, RngSymbol.Blank,RngSymbol.Blank, RngSymbol.Blank,
                RngSymbol.Blank, RngSymbol.Blank, RngSymbol.Blank, RngSymbol.Blank));
        List<RngSymbol> reel4 =gc.getBuyFeatureReels().get(reelComboPattern.get(3)).getReel4();
        List<RngSymbol> reel5 =gc.getBuyFeatureReels().get(reelComboPattern.get(4)).getReel5();
        List<RngSymbol> reel6 =gc.getBuyFeatureReels().get(reelComboPattern.get(5)).getReel6();
        SlotReel<RngSymbol> slotReel0 = new SlotReel<>();
        slotReel0.setSymbols(reel0);
        SlotReel<RngSymbol> slotReel1 = new SlotReel<>();
        slotReel1.setSymbols(reel1);
        SlotReel<RngSymbol> slotReel2 = new SlotReel<>();
        slotReel2.setSymbols(reel2);
        SlotReel<RngSymbol> slotReel3 = new SlotReel<>();
        slotReel3.setSymbols(reel3);
        SlotReel<RngSymbol> slotReel4 = new SlotReel<>();
        slotReel4.setSymbols(reel4);
        SlotReel<RngSymbol> slotReel5 = new SlotReel<>();
        slotReel5.setSymbols(reel5);
        SlotReel<RngSymbol> slotReel6 = new SlotReel<>();
        slotReel6.setSymbols(reel6);
        reels.add(slotReel0);reels.add(slotReel1);reels.add(slotReel2);reels.add(slotReel3);reels.add(slotReel4);reels.add(slotReel5);reels.add(slotReel6);
        reelLayout.setReels(reels);
//        List<Integer>         topReelIndex             =      getReelSpinner().reelSpin(gc.getReelLayoutConfiguration(), reelLayout.getReels());
        List<Integer>         topReelIndex               = new ArrayList<>();
        for(int i=0;i<reelComboPattern.size();i++){
            if(i==0) topReelIndex.add(reelComboPattern.get(i)==0 ? randomNumbersWithKey.get("reel0") : randomNumbersWithKey.get("reel0_0"));
            if(i==1) topReelIndex.add(reelComboPattern.get(i)==0 ? randomNumbersWithKey.get("reel1") : randomNumbersWithKey.get("reel0_1"));
            if(i==2) topReelIndex.add(reelComboPattern.get(i)==0 ? randomNumbersWithKey.get("reel2") : randomNumbersWithKey.get("reel0_2"));
            if(i==3) topReelIndex.add(reelComboPattern.get(i)==0 ? randomNumbersWithKey.get("reel4") : randomNumbersWithKey.get("reel0_4"));
            if(i==4) topReelIndex.add(reelComboPattern.get(i)==0 ? randomNumbersWithKey.get("reel5") : randomNumbersWithKey.get("reel0_5"));
            if(i==5) topReelIndex.add(reelComboPattern.get(i)==0 ? randomNumbersWithKey.get("reel6") : randomNumbersWithKey.get("reel0_6"));
        }
        topReelIndex.add(3,0);

        SlotsSpinGameActivity slotSpinActivity           =      new SlotsSpinGameActivity(
                gc.getColumns(),
                gc.getRows(),
                topReelIndex,
                slotsGamePlayState.getGameStatus(),
                reelLayout.getIndex(),
                BonusContext
                        .copyFromPreviousOrCreateNew(previousContext,
                                RngBonusContext::new,
                                RngBonusContext::new));

        CustomReelsSymbolGrid customReelsSymbolGrid = new CustomReelsSymbolGrid(
                gc.getColumns(),
                gc.getRows(),
                topReelIndex,
                slotsGamePlayState.getGameStatus(),
                BonusContext
                        .copyFromPreviousOrCreateNew(previousContext,
                                RngBonusContext::new,
                                RngBonusContext::new));

        SymbolGrid symbolGrid               = customReelsSymbolGrid.prepareSymbolGrid(reelLayout);

        int[] lockedReels={3};
        symbolGrid.setLockedReels(lockedReels);

        RngBonusContext bonusContext       = (RngBonusContext) slotSpinActivity.getBonusContext();

        List<List<Integer>> wildPosition=new ArrayList<>();

        bonusContext.setSpecialSymbolPosition(wildPosition);
        consumeBonus((RngBonusContext) slotSpinActivity.getBonusContext());
        BigDecimal          bonusMultiplier = bonusContext.getBonusMultiplier();
        List<RngSymbol>    wildSymbols     = new LinkedList<>();
        wildSymbols.add(RngSymbol.WD);
        PayStep payStep         = new PayStep(symbolGrid);
        slotSpinActivity.addPayStep(payStep);
        List<RngSymbol> nonReplaceableSymbols=new ArrayList<>(Arrays.asList(RngSymbol.SC, RngSymbol.Blank));
        List<PayWay> payWays = payWaysHandler.findPayWays(
                slotSpinActivity.getLastPayStep().getStepSymbolGrid(),
                wildSymbols,
                nonReplaceableSymbols,
                gc.getSymbolStakeMultipliers(),
                bonusMultiplier);
        if(payWays.size()>0){
            for(int i=0;i<payWays.size();i++){
                payWays.get(i).getData().put("Direction",new String("LeftToRight"));
            }
        }
        LinkedMultiValueMap<Integer, Symbol> reverseSymbolGrid= FeatureUtils.reverseSymbolGrid(symbolGrid.getSymbolGrid());
        symbolGrid.setSymbolGrid(reverseSymbolGrid);
        List<PayWay> payWays1 =payWaysHandler.findPayWays(
                symbolGrid,
                wildSymbols,
                nonReplaceableSymbols,
                gc.getSymbolStakeMultipliers(),
                bonusMultiplier);
        if(payWays1.size()>0){
            for(int i=0;i<payWays1.size();i++){
                payWays1.get(i).getData().put("Direction",new String("RightToLeft"));
            }
        }
        LinkedMultiValueMap<Integer, Symbol> reverseSymbolGrid1=FeatureUtils.reverseSymbolGrid(symbolGrid.getSymbolGrid());
        symbolGrid.setSymbolGrid(reverseSymbolGrid1);
        payWays.addAll(payWays1);
        payStep.setPayWays(payWays);

        awardBonus(slotSpinActivity,gc,gameBonus);

        slotsGamePlayState.setBonusContext(slotSpinActivity.getBonusContext());
        gamePlay.setGamePlayState(slotsGamePlayState);
        // update game round status
        checkGameStatus(gamePlay);
        slotSpinActivity.setGameStatus(gamePlay.getGamePlayState().getGameStatus());
        gamePlay.setStatus(gamePlay.getGamePlayState().getGameStatus());
        //end
        return slotSpinActivity;

    }

        private SlotsSpinGameActivity processBaseSpin(GamePlay gamePlay, RngGameBonus gameBonus) {
            SlotsGamePlayState slotsGamePlayState          = (SlotsGamePlayState) gamePlay.getGamePlayState();
            RngGameConfiguration gc                          = this.gc;

            RandomPoolConfig randomPoolConfig = gc.getRandomConfiguration().get(gameBonus);
            int randomsRequired = randomPoolConfig.getNumbers();
            HashMap<String, Integer> randomList = randomPoolConfig.getRandomList();
            Map<String, Integer> randomNumbersWithKey = rng.randomProvider(randomList);
            if(randomNumbersWithKey.size()!=randomsRequired) throw new GameEngineException("required random numbers not provided");

            RngBonusContext previousContext     = (RngBonusContext) slotsGamePlayState.getBonusContext();
            int layoutPick = randomNumbersWithKey.get("reelLayout");
            ReelLayout<RngSymbol, RngGameBonus> reelLayout    = layoutSelector.selectReelLayout(gameBonus, gc.getReelLayoutConfiguration(),layoutPick);
//            List<Integer> topReelIndex              =     getReelSpinner().reelSpin(gc.getReelLayoutConfiguration(), reelLayout.getReels());

            List<Integer>         topReelIndex               = new ArrayList<>();
            topReelIndex.add(randomNumbersWithKey.get("reel0"));
            topReelIndex.add(randomNumbersWithKey.get("reel1"));
            topReelIndex.add(randomNumbersWithKey.get("reel2"));
            topReelIndex.add(0);
            topReelIndex.add(randomNumbersWithKey.get("reel4"));
            topReelIndex.add(randomNumbersWithKey.get("reel5"));
            topReelIndex.add(randomNumbersWithKey.get("reel6"));

            SlotsSpinGameActivity slotSpinActivity          =     new SlotsSpinGameActivity(
                                                                             gc.getColumns(),
                                                                             gc.getRows(),
                                                                             topReelIndex,
                                                                             slotsGamePlayState.getGameStatus(),
                                                                             reelLayout.getIndex(),
                                                                             BonusContext
                                                                                     .copyFromPreviousOrCreateNew(previousContext,
                                                                                             RngBonusContext::new,
                                                                                             RngBonusContext::new));
            SymbolGrid  symbolGrid         = slotSpinActivity.prepareSymbolGrid(gc.getReelLayoutConfiguration());

            if(gameBonus.equals(RngGameBonus.NONE)){
                int[] lockedReels={3};
                symbolGrid.setLockedReels(lockedReels);
            }
            RngBonusContext bonusContext       = (RngBonusContext) slotSpinActivity.getBonusContext();
            if(gameBonus.equals(RngGameBonus.FREE_SPINS) || gameBonus.equals(RngGameBonus.BUY_FEATURE_FREE_SPINS)){
                int highValueSymbolPick = randomNumbersWithKey.get("middleReelSymbolPicker");
                Symbol replacementSymbol=pickByWeightage.pick(gc.getFreeSpinSymbolOption(),highValueSymbolPick).getSymbol();
                for(int i=0;i<symbolGrid.getSymbolGrid().size();i++){
                    for(int j=0;j<symbolGrid.getSymbolGrid().get(i).size();j++){
                        if(symbolGrid.getSymbolGrid().get(i).get(j).equals(RngSymbol.Blank)) symbolGrid.getSymbolGrid().get(i).set(j,replacementSymbol);
                    }
                }
            }
            int lokiCount=0,thorCount=0,scatterCount=0;
            for(int i=0;i<symbolGrid.getSymbolGrid().size();i++){
                for(int j=0;j<symbolGrid.getSymbolGrid().get(i).size();j++){
                    if(symbolGrid.getSymbolGrid().get(i).get(j).equals(RngSymbol.LO)) lokiCount++;
                    if(symbolGrid.getSymbolGrid().get(i).get(j).equals(RngSymbol.TH)) thorCount++;
                    if(symbolGrid.getSymbolGrid().get(i).get(j).equals(RngSymbol.SC)) scatterCount++;
                }
            }
            List<List<Integer>> wildPosition=new ArrayList<>();
            if(lokiCount>0 && gameBonus.equals(RngGameBonus.NONE)){
                int reelno=randomNumbersWithKey.get("wildGenerator")+4;
                List<Integer> wildList=new ArrayList<>();
                for(int j=0;j<symbolGrid.getSymbolGrid().get(reelno).size();j++){
                    symbolGrid.getSymbolGrid().get(reelno).set(j, RngSymbol.WD);
                    wildList.add(j);
                }
                wildPosition.add(wildList);
            }
            else if(lokiCount>0 && (gameBonus.equals(RngGameBonus.FREE_SPINS)|| gameBonus.equals(RngGameBonus.BUY_FEATURE_FREE_SPINS))){
                int reelno=randomNumbersWithKey.get("wildGenerator");
                if(reelno<2) reelno=reelno+1;
                else reelno=reelno+2;
                List<Integer> wildList=new ArrayList<>();
                for(int j=0;j<symbolGrid.getSymbolGrid().get(reelno).size();j++){
                    symbolGrid.getSymbolGrid().get(reelno).set(j, RngSymbol.WD);
                    wildList.add(j);
                }
                wildPosition.add(wildList);
            }
            bonusContext.setSpecialSymbolPosition(wildPosition);
            consumeBonus((RngBonusContext) slotSpinActivity.getBonusContext());
            BigDecimal          bonusMultiplier = bonusContext.getBonusMultiplier();
            List<RngSymbol>    wildSymbols     = new LinkedList<>();
            wildSymbols.add(RngSymbol.WD);
            PayStep payStep         = new PayStep(symbolGrid);
            slotSpinActivity.addPayStep(payStep);
            List<RngSymbol> nonReplaceableSymbols=new ArrayList<>(Arrays.asList(RngSymbol.SC, RngSymbol.Blank));
            List<PayWay> payWays = payWaysHandler.findPayWays(
                    slotSpinActivity.getLastPayStep().getStepSymbolGrid(),
                    wildSymbols,
                    nonReplaceableSymbols,
                    gc.getSymbolStakeMultipliers(),
                    bonusMultiplier);
            if(payWays.size()>0){
                for(int i=0;i<payWays.size();i++){
                    payWays.get(i).getData().put("Direction",new String("LeftToRight"));
                }
            }
            LinkedMultiValueMap<Integer, Symbol> reverseSymbolGrid= FeatureUtils.reverseSymbolGrid(symbolGrid.getSymbolGrid());
            symbolGrid.setSymbolGrid(reverseSymbolGrid);
            List<PayWay> payWays1 =payWaysHandler.findPayWays(
                    symbolGrid,
                    wildSymbols,
                    nonReplaceableSymbols,
                    gc.getSymbolStakeMultipliers(),
                    bonusMultiplier);
            if(payWays1.size()>0){
                for(int i=0;i<payWays1.size();i++){
                    payWays1.get(i).getData().put("Direction",new String("RightToLeft"));
                }
            }
            LinkedMultiValueMap<Integer, Symbol> reverseSymbolGrid1=FeatureUtils.reverseSymbolGrid(symbolGrid.getSymbolGrid());
            symbolGrid.setSymbolGrid(reverseSymbolGrid1);
            payWays.addAll(payWays1);
            payStep.setPayWays(payWays);
            if(scatterCount>2 && gameBonus.equals(RngGameBonus.NONE)) awardBonus(slotSpinActivity,gc,gameBonus);
            if( thorCount>0 ) {
                if(gameBonus.equals(RngGameBonus.NONE)) bonusContext.setBonusAwarded(RngGameBonus.NONE_RESPIN);
                if(gameBonus.equals(RngGameBonus.FREE_SPINS)) bonusContext.setBonusAwarded(RngGameBonus.FREE_RESPIN);
                if(gameBonus.equals(RngGameBonus.BUY_FEATURE_FREE_SPINS)) bonusContext.setBonusAwarded(RngGameBonus.BUY_FEATURE_RESPIN);
            }
            slotsGamePlayState.setBonusContext(slotSpinActivity.getBonusContext());
            gamePlay.setGamePlayState(slotsGamePlayState);
            // update game round status
            checkGameStatus(gamePlay);
            slotSpinActivity.setGameStatus(gamePlay.getGamePlayState().getGameStatus());
            gamePlay.setStatus(gamePlay.getGamePlayState().getGameStatus());
            //end
            return slotSpinActivity;
        }

        private SlotsSpinGameActivity processReSpin(GamePlay gamePlay, RngGameBonus gameBonus) {
            SlotsGamePlayState slotsGamePlayState          = (SlotsGamePlayState) gamePlay.getGamePlayState();
            RngGameConfiguration gc                          = this.gc;

            RandomPoolConfig randomPoolConfig = gc.getRandomConfiguration().get(gameBonus);
            int randomsRequired = randomPoolConfig.getNumbers();
            HashMap<String, Integer> randomList = randomPoolConfig.getRandomList();
            Map<String, Integer> randomNumbersWithKey = rng.randomProvider(randomList);
            if(randomNumbersWithKey.size()!=randomsRequired) throw new GameEngineException("required random numbers not provided");

            int layoutPick = randomNumbersWithKey.get("reelLayout");
            ReelLayout<RngSymbol, RngGameBonus> reelLayout    = layoutSelector.selectReelLayout(gameBonus, gc.getReelLayoutConfiguration(),layoutPick);

            List<Integer>         topReelIndex               = new ArrayList<>();
            if(gameBonus.equals(RngGameBonus.NONE_RESPIN)){
                topReelIndex.add(randomNumbersWithKey.get("reel0"));
                topReelIndex.add(randomNumbersWithKey.get("reel1"));
                topReelIndex.add(randomNumbersWithKey.get("reel2"));
                topReelIndex.add(0);
                topReelIndex.add(0);
                topReelIndex.add(0);
                topReelIndex.add(0);
            }
            else {
                topReelIndex.add(randomNumbersWithKey.get("reel0"));
                topReelIndex.add(randomNumbersWithKey.get("reel1"));
                topReelIndex.add(randomNumbersWithKey.get("reel2"));
                topReelIndex.add(0);
                topReelIndex.add(randomNumbersWithKey.get("reel4"));
                topReelIndex.add(randomNumbersWithKey.get("reel5"));
                topReelIndex.add(randomNumbersWithKey.get("reel6"));
            }



//            List<Integer>                   topReelIndex        = getReelSpinner().reelSpin(gc.getReelLayoutConfiguration(), reelLayout.getReels());
            RngBonusContext previousContext     = (RngBonusContext) slotsGamePlayState.getBonusContext();
            SlotsSpinGameActivity slotSpinActivity    = new SlotsSpinGameActivity(
                    gc.getColumns(), gc.getRows(), topReelIndex,
                    slotsGamePlayState.getGameStatus(), reelLayout.getIndex(), BonusContext
                    .copyFromPreviousOrCreateNew(previousContext, RngBonusContext::new, RngBonusContext::new));
            SymbolGrid                           symbolGrid         = slotSpinActivity.prepareSymbolGrid(gc.getReelLayoutConfiguration());
            RngBonusContext bonusContext       = (RngBonusContext) slotSpinActivity.getBonusContext();
            if(previousContext.getFreeSpinsContext()!=null) bonusContext.setFreeSpinsContext(previousContext.getFreeSpinsContext());
            BigDecimal          bonusMultiplier = bonusContext.getBonusMultiplier();
            List<RngSymbol>    wildSymbols     = new LinkedList<>();
            wildSymbols.add(RngSymbol.WD);
            PayStep payStep         = new PayStep(symbolGrid);
            slotSpinActivity.addPayStep(payStep);
            List<RngSymbol> nonReplaceableSymbols=new ArrayList<>(Arrays.asList(RngSymbol.SC, RngSymbol.Blank));
            if(gameBonus.equals(RngGameBonus.NONE_RESPIN)){
                int[] lockedReels={3,4,5,6};
                symbolGrid.setLockedReels(lockedReels);
                List<PayWay> payWays = payWaysHandler.findPayWays(
                        slotSpinActivity.getLastPayStep().getStepSymbolGrid(),
                        wildSymbols,
                        nonReplaceableSymbols,
                        gc.getSymbolStakeMultipliers(),
                        bonusMultiplier);
                if(payWays.size()>0){
                    for(int i=0;i<payWays.size();i++){
                        payWays.get(i).getData().put("Direction",new String("LeftToRight"));
                    }
                }
                payStep.setPayWays(payWays);
            }
            else if(gameBonus.equals(RngGameBonus.FREE_RESPIN) || gameBonus.equals(RngGameBonus.BUY_FEATURE_RESPIN)){
                int pick = randomNumbersWithKey.get("middleReelSymbolPicker");
                Symbol replacementSymbol=pickByWeightage.pick(gc.getFreeReSpinSymbolOption(),pick).getSymbol();
                for(int i=0;i<symbolGrid.getSymbolGrid().size();i++){
                    for(int j=0;j<symbolGrid.getSymbolGrid().get(i).size();j++){
                        if(symbolGrid.getSymbolGrid().get(i).get(j).equals(RngSymbol.Blank)) symbolGrid.getSymbolGrid().get(i).set(j,replacementSymbol);
                    }
                }
                List<PayWay> payWays = payWaysHandler.findPayWays(
                        slotSpinActivity.getLastPayStep().getStepSymbolGrid(),
                        wildSymbols,
                        nonReplaceableSymbols,
                        gc.getSymbolStakeMultipliers(),
                        bonusMultiplier);
                if(payWays.size()>0){
                    for(int i=0;i<payWays.size();i++){
                        payWays.get(i).getData().put("Direction",new String("LeftToRight"));
                    }
                }
                LinkedMultiValueMap<Integer, Symbol> reverseSymbolGrid=FeatureUtils.reverseSymbolGrid(symbolGrid.getSymbolGrid());
                symbolGrid.setSymbolGrid(reverseSymbolGrid);
                List<PayWay> payWays1 =payWaysHandler.findPayWays(
                        symbolGrid,
                        wildSymbols,
                        nonReplaceableSymbols,
                        gc.getSymbolStakeMultipliers(),
                        bonusMultiplier);
                if(payWays1.size()>0){
                    for(int i=0;i<payWays1.size();i++){
                        payWays1.get(i).getData().put("Direction",new String("RightToLeft"));
                    }
                }
                LinkedMultiValueMap<Integer, Symbol> reverseSymbolGrid1=FeatureUtils.reverseSymbolGrid(symbolGrid.getSymbolGrid());
                symbolGrid.setSymbolGrid(reverseSymbolGrid1);
                payWays.addAll(payWays1);
                payStep.setPayWays(payWays);
            }
            if(bonusContext.getFreeSpinsContext() != null && bonusContext.getFreeSpinsContext().getFreeSpinsRemaining() > 0) {
                if(gameBonus.equals(RngGameBonus.NONE_RESPIN) || gameBonus.equals(RngGameBonus.FREE_RESPIN)) bonusContext.setBonusAwarded(RngGameBonus.FREE_SPINS);
                if(gameBonus.equals(RngGameBonus.BUY_FEATURE_RESPIN)) bonusContext.setBonusAwarded(RngGameBonus.BUY_FEATURE_FREE_SPINS);
            } else bonusContext.setBonusAwarded(RngGameBonus.NONE);
            slotSpinActivity.setGameStatus(gamePlay.getGamePlayState().getGameStatus());
            gamePlay.setStatus(gamePlay.getGamePlayState().getGameStatus());
            slotsGamePlayState.setBonusContext(slotSpinActivity.getBonusContext());
            gamePlay.setGamePlayState(slotsGamePlayState);
            // update game round status
            checkGameStatus(gamePlay);
            slotSpinActivity.setGameStatus(gamePlay.getGamePlayState().getGameStatus());
            gamePlay.setStatus(gamePlay.getGamePlayState().getGameStatus());
            //end
            return slotSpinActivity;
        }

        private void awardBonus(SlotsSpinGameActivity spinGameActivity, RngGameConfiguration gc, RngGameBonus gameBonus) {

            //scatter wins
            SlotsPay scatterPay = scatterPayHandler
                    .awardBonus(spinGameActivity.getLastPayStep().getStepSymbolGrid(),
                            RngSymbol.SC,
                            gc.getBonusGameConfiguration().getScatterStakeMultipliers());
            //award free games
            RngBonusContext bonusContext = (RngBonusContext) spinGameActivity.getBonusContext();
            //award free games
            if (scatterPay!=null && scatterPay.getSymbolCount() >= 3) {
                spinGameActivity.getLastPayStep().setScatterPay(scatterPay);
                if(gc.getBonusGameConfiguration().getFreeGameConfiguration().getAwardGameBonus()!=null) {
                    SlotsFreeGameConfiguration<RngGameBonus> fgc = gc.getBonusGameConfiguration()
                            .getFreeGameConfiguration();
                    if(gameBonus.equals(RngGameBonus.BUY_FEATURE)){
                        fgc.setAwardGameBonus(RngGameBonus.BUY_FEATURE_FREE_SPINS);
                    }
                    if(gameBonus.equals(RngGameBonus.NONE)){
                        fgc.setAwardGameBonus(RngGameBonus.FREE_SPINS);
                    }
                    RngGameBonus bonusAwarded =
                            freeSpinBonusHandler.awardBonus((SlotsBonusContext) spinGameActivity.getBonusContext(),
                                    scatterPay.getSymbolCount(), fgc);
                    bonusContext.setBonusMultiplier(fgc.getMultiplier());
                    spinGameActivity.setBonusAwarded(bonusAwarded);
                }
            }
        }

        private void consumeBonus(RngBonusContext bonusContext) throws GameEngineException {
            if (bonusContext.getFreeSpinsContext() != null &&
                    bonusContext.getFreeSpinsContext().getFreeSpinsRemaining() > 0) {
                FreeSpinsContext freeSpinsContext = freeSpinBonusHandler
                        .consumeBonus(bonusContext.getFreeSpinsContext());
                bonusContext.setFreeSpinsContext(freeSpinsContext);
            }
        }

        private void checkGameStatus(GamePlay gamePlay) {
            BonusContext bonusContext = gamePlay.getGamePlayState().getBonusContext();
            if (bonusContext.updateGameBonusAwarded(gamePlay)) {
                gamePlay.getGamePlayState().setGameStatus(GameStatus.INPROGRESS);
            } else {
                gamePlay.getGamePlayState().setGameStatus(GameStatus.COMPLETED);
            }
        }

        private BigDecimal getTotalStake(BigDecimal stakePerLine, int noOfLines) {
            return GamePlay.scaledValue(stakePerLine.multiply(BigDecimal.valueOf(noOfLines)));
        }

        public GameEngineModule getGameEngineModule() {
               return new EngineModule();
           }

}
