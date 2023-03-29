package com.feg.games.ClashOfMighty.ext.slots.handlers;

import com.feg.games.ClashOfMighty.ext.slots.symbol.PaySymbolHandler;
import com.feg.games.ClashOfMighty.ext.slots.bonus.handler.PayLineBonusMultiplierHandler;
import com.feg.games.ClashOfMighty.ext.slots.bonus.handler.PayLineHandler;
import com.feg.games.ClashOfMighty.ext.slots.model.PayLine;
import com.feg.games.ClashOfMighty.ext.slots.model.SymbolLine;
import com.feg.games.ClashOfMighty.ext.slots.reels.SymbolGrid;
import com.feg.games.ClashOfMighty.ext.api.symbol.Symbol;
import com.feg.games.ClashOfMighty.ext.api.symbol.SymbolType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;


@Component
public class DefaultPayLineHandler<TSymbol extends Symbol> {

    PayLineHandler defaultPayLineHandler = (payLine, payLineOffsetList, paySymbol, initSymbolLine, initialSymbolLineMultiplier, modifiedSymbolLine, modifiedSymbolLineMultiplier, payLineBonusMultiplierHandler)->{
        if (initialSymbolLineMultiplier.compareTo(BigDecimal.ZERO) <= 0 && modifiedSymbolLineMultiplier.compareTo(BigDecimal.ZERO) <= 0) {
            return null;
        }

        PayLine winningLine = null;
        if (initialSymbolLineMultiplier.compareTo(modifiedSymbolLineMultiplier) > 0) {
            int symbolCount = initSymbolLine.getKindASymbolCount(true);
            winningLine = new PayLine(paySymbol, payLine, payLineOffsetList, initSymbolLine,
                    initSymbolLine, initialSymbolLineMultiplier,
                    payLineBonusMultiplierHandler.getBonusMultiplier(initSymbolLine, initSymbolLine),
                    symbolCount);
        } else {
            int symbolCount = modifiedSymbolLine.getKindASymbolCount(true);
            winningLine = new PayLine(paySymbol, payLine, payLineOffsetList, initSymbolLine,
                    modifiedSymbolLine, modifiedSymbolLineMultiplier,
                    payLineBonusMultiplierHandler.getBonusMultiplier(initSymbolLine, modifiedSymbolLine),
                    symbolCount);
        }

        return winningLine;
    };

    public List<PayLine> findPayLines(SymbolGrid symbolGrid,
                                      int[][] payLineOffsets,
                                      SymbolLine.LineDirection lineDirection,
                                      int playingLines,
                                      List<TSymbol> replacingSymbols,
                                      List<TSymbol> nonReplaceableSymbols,
                                      boolean adjacent,
                                      LinkedMultiValueMap<? extends Symbol, BigDecimal> symbolStakeMultipliers,
                                      PaySymbolHandler paySymbolHandler,
                                      PayLineHandler scatterPayLineHandler,
                                      PayLineBonusMultiplierHandler payLineBonusMultiplierHandler) {
        if (lineDirection == null)
            lineDirection = SymbolLine.LineDirection.LEFT;

        List<PayLine> payLines = new LinkedList<>();

        //Process each pay line in the pay line grid offsets from the configuration
        int[] payLineOffset;
        List<SymbolLine> symbolLines = new LinkedList<>();
        SymbolLine initialSymbolLine = null;
        List<PayLine> winningLines = null;
        for (int payLineIndex = 0; payLineIndex < playingLines; payLineIndex++) {
            symbolLines.clear();
            payLineOffset = payLineOffsets[payLineIndex];
            int payLine = payLineIndex + 1;

            initialSymbolLine = symbolGrid.extractSymbolLineFromPayLineOffsets(payLineOffset);

            if (lineDirection == SymbolLine.LineDirection.BOTH) {
                initialSymbolLine.setLineDirection(SymbolLine.LineDirection.LEFT);
                symbolLines.add(initialSymbolLine);
                SymbolLine clone = new SymbolLine(initialSymbolLine);
                clone.setLineDirection(SymbolLine.LineDirection.RIGHT);
                symbolLines.add(clone);
            } else {
                initialSymbolLine.setLineDirection(lineDirection);
                symbolLines.add(initialSymbolLine);
            }

            winningLines = getWinningLines(symbolLines,
                    replacingSymbols,
                    nonReplaceableSymbols,
                    adjacent,
                    symbolStakeMultipliers,
                    paySymbolHandler,
                    payLineBonusMultiplierHandler,
                    scatterPayLineHandler,
                    payLine,
                    payLineOffset);
            if (!winningLines.isEmpty()) {
                payLines.addAll(winningLines);
            }
        }
        return payLines;
    }

    private List<PayLine> getWinningLines(List<SymbolLine> symbolLines, List<? extends Symbol> replacingSymbols,
                                          List<? extends Symbol> nonReplaceableSymbols, boolean adjacent,
                                          LinkedMultiValueMap<? extends Symbol, BigDecimal> symbolStakeMultipliers,
                                          PaySymbolHandler paySymbolHandler,
                                          PayLineBonusMultiplierHandler payLineBonusMultiplierHandler,
                                          PayLineHandler scatterPayLineHandler,
                                          int payLine, int[] payLineOffsetList) {

        List<PayLine> winningLines = new LinkedList<>();



        for (SymbolLine initSymbolLine : symbolLines) {
            Symbol paySymbol = paySymbolHandler != null
                    ? paySymbolHandler.getPaySymbol(initSymbolLine)
                    : initSymbolLine.getPaySymbol();

            BigDecimal initialSymbolLineMultiplier = initSymbolLine.calculateMultiplier(paySymbol, adjacent, symbolStakeMultipliers);

            SymbolLine modifiedSymbolLine = null;
            BigDecimal modifiedSymbolLineMultiplier = null;
            if(!replacingSymbols.isEmpty()){
                modifiedSymbolLine = paySymbol != null
                        ? initSymbolLine.replaceSymbols(replacingSymbols, nonReplaceableSymbols, paySymbol)
                        : initSymbolLine;
                modifiedSymbolLine.setPayLineOffSet(initSymbolLine.getPayLineOffSet());
                modifiedSymbolLine.setLineDirection(initSymbolLine.getLineDirection());
                //determine the multiplier on the regular line without wilds replaced and line with wilds replaced

                modifiedSymbolLineMultiplier = modifiedSymbolLine.calculateMultiplier(paySymbol, adjacent, symbolStakeMultipliers);
            }

            //If the multiplier on both regular line and wilds replaced line are 0,
            //then this is not a winning line and proceed with the next pay line for evaluation
            PayLine winningLine = null;
            if(paySymbol!=null && paySymbol.getSymbolType().contains(SymbolType.SCATTER) && scatterPayLineHandler!=null){
                winningLine = scatterPayLineHandler.getWinningLine(payLine,
                        payLineOffsetList,
                        paySymbol,
                        initSymbolLine,
                        initialSymbolLineMultiplier,
                        modifiedSymbolLine==null?initSymbolLine:modifiedSymbolLine,
                        modifiedSymbolLineMultiplier==null?initialSymbolLineMultiplier:modifiedSymbolLineMultiplier,
                        payLineBonusMultiplierHandler);
            }else{
                //normal symbol payline handler
                winningLine = defaultPayLineHandler.getWinningLine(payLine,
                        payLineOffsetList,
                        paySymbol,
                        initSymbolLine,
                        initialSymbolLineMultiplier,
                        modifiedSymbolLine==null?initSymbolLine:modifiedSymbolLine,
                        modifiedSymbolLineMultiplier==null?initialSymbolLineMultiplier:modifiedSymbolLineMultiplier,
                        payLineBonusMultiplierHandler);
            }

            if(winningLine==null)
                continue;

            winningLines.add(winningLine);
        }

        return winningLines;
    }
}
