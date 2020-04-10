package highlighter;

import com.intellij.codeInsight.daemon.*;
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder;
import com.intellij.psi.*;
import highlighter.CustomIcons;
import org.jetbrains.annotations.NotNull;
import pluginResources.HighlightSingleton;

import java.util.*;

public class CustomLineMarker extends RelatedItemLineMarkerProvider {
    @Override
    protected void collectNavigationMarkers( @NotNull PsiElement element,
                                             @NotNull Collection< ? super RelatedItemLineMarkerInfo > result ) {

            if (!HighlightSingleton.getInstance().getMethodsHeaders().isEmpty()){
                for (PsiElement elem : HighlightSingleton.getInstance().getMethodsHeaders()){
                    NavigationGutterIconBuilder< PsiElement > builder =
                            NavigationGutterIconBuilder.create(CustomIcons.FAIL_GUTTER)
                                    //TODO ADD PSI ELEMENT
                                    .setTargets( elem )
                                    .setTooltipText( "Navigate to Simple language property" );
                    result.add( builder.createLineMarkerInfo( element ) );

                }

            }
    }

}