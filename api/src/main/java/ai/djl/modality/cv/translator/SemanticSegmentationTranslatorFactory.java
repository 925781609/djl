/*
 * Copyright 2022 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance
 * with the License. A copy of the License is located at
 *
 * http://aws.amazon.com/apache2.0/
 *
 * or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 */
package ai.djl.modality.cv.translator;

import ai.djl.Model;
import ai.djl.modality.Input;
import ai.djl.modality.Output;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.translator.wrapper.FileTranslator;
import ai.djl.modality.cv.translator.wrapper.InputStreamTranslator;
import ai.djl.modality.cv.translator.wrapper.UrlTranslator;
import ai.djl.translate.Translator;
import ai.djl.translate.TranslatorFactory;
import ai.djl.util.Pair;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.URL;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/** A {@link TranslatorFactory} that creates a {@link SemanticSegmentationTranslator} instance. */
public class SemanticSegmentationTranslatorFactory implements TranslatorFactory {

    private static final Set<Pair<Type, Type>> SUPPORTED_TYPES = new HashSet<>();

    static {
        SUPPORTED_TYPES.add(new Pair<>(Image.class, Image.class));
    }

    /** {@inheritDoc} */
    @Override
    public Translator<?, ?> newInstance(
            Class<?> input, Class<?> output, Model model, Map<String, ?> arguments) {
        if (input == Image.class && output == Image.class) {
            return SemanticSegmentationTranslator.builder(arguments).build();
        } else if (input == Path.class && output == Image.class) {
            return new FileTranslator<>(SemanticSegmentationTranslator.builder(arguments).build());
        } else if (input == URL.class && output == Image.class) {
            return new UrlTranslator<>(SemanticSegmentationTranslator.builder(arguments).build());
        } else if (input == InputStream.class && output == Image.class) {
            return new InputStreamTranslator<>(
                    SemanticSegmentationTranslator.builder(arguments).build());
        } else if (input == Input.class && output == Output.class) {
            return new ImageServingTranslator(
                    SemanticSegmentationTranslator.builder(arguments).build());
        }
        throw new IllegalArgumentException("Unsupported input/output types.");
    }

    /** {@inheritDoc} */
    @Override
    public Set<Pair<Type, Type>> getSupportedTypes() {
        return SUPPORTED_TYPES;
    }
}
