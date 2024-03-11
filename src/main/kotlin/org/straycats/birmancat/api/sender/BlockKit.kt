package org.straycats.birmancat.api.sender

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

/**
 * use block kit builder
 * @ref https://app.slack.com/block-kit-builder
 */
class BlockKit {

    @JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type",
        visible = true,
    )
    @JsonSubTypes(
        JsonSubTypes.Type(value = org.straycats.birmancat.api.sender.BlockKit.Context::class, name = "context"),
        JsonSubTypes.Type(value = org.straycats.birmancat.api.sender.BlockKit.Header::class, name = "header"),
        JsonSubTypes.Type(value = org.straycats.birmancat.api.sender.BlockKit.Section::class, name = "section"),
        JsonSubTypes.Type(value = org.straycats.birmancat.api.sender.BlockKit.Divider::class, name = "divider"),
        JsonSubTypes.Type(value = org.straycats.birmancat.api.sender.BlockKit.Image::class, name = "checkboxes"),
        JsonSubTypes.Type(value = org.straycats.birmancat.api.sender.BlockKit.Text::class, name = "plain_text"),
        JsonSubTypes.Type(value = org.straycats.birmancat.api.sender.BlockKit.Text::class, name = "mrkdwn"),
        JsonSubTypes.Type(value = org.straycats.birmancat.api.sender.BlockKit.Button::class, name = "button"),
        JsonSubTypes.Type(value = org.straycats.birmancat.api.sender.BlockKit.Checkbox::class, name = "checkboxes"),
        JsonSubTypes.Type(value = org.straycats.birmancat.api.sender.BlockKit.Image::class, name = "image"),
    )
    interface Block

    /**
     * @ref https://api.slack.com/reference/block-kit#elements
     */
    interface BlockKitElement : org.straycats.birmancat.api.sender.BlockKit.Block

    /**
     * https://api.slack.com/reference/block-kit#objects
     */

    interface BlockKitObject : org.straycats.birmancat.api.sender.BlockKit.Block

    /**
     * @ref https://api.slack.com/reference/block-kit#blocks
     */
    interface BlockKitBlock : org.straycats.birmancat.api.sender.BlockKit.Block

    data class Context(
        /** Only images BlockElement and text CompositionObject are allowed.*/
        val elements: List<org.straycats.birmancat.api.sender.BlockKit.BlockKitObject>,
        val blockId: String? = null,
    ) : org.straycats.birmancat.api.sender.BlockKit.BlockKitBlock {
        val type = "context"
    }

    data class Header(
        val text: org.straycats.birmancat.api.sender.BlockKit.Text,
    ) : org.straycats.birmancat.api.sender.BlockKit.BlockKitBlock {
        val type: String = "header"
    }

    data class Section(
        val text: org.straycats.birmancat.api.sender.BlockKit.Text? = null,
        val blockId: String? = null,
        val fields: List<org.straycats.birmancat.api.sender.BlockKit.Text>? = null,
        val accessory: org.straycats.birmancat.api.sender.BlockKit.BlockKitElement? = null,
    ) : org.straycats.birmancat.api.sender.BlockKit.BlockKitBlock {
        val type = "section"
    }

    class Divider : org.straycats.birmancat.api.sender.BlockKit.BlockKitBlock {
        val type = "divider"
    }

    data class Button(
        val text: org.straycats.birmancat.api.sender.BlockKit.Text,
        val actionId: String,
        val url: String? = null,
        val value: String? = null,
        val style: org.straycats.birmancat.api.sender.BlockKit.Button.Style? = null,
        val confirm: org.straycats.birmancat.api.sender.BlockKit.ConfirmationDialog? = null,
        val accessibilityLabel: org.straycats.birmancat.api.sender.BlockKit.Text? = null,
    ) : org.straycats.birmancat.api.sender.BlockKit.BlockKitElement {
        val type = "button"

        enum class Style {
            @JsonProperty("primary")
            PRIMARY,

            @JsonProperty("danger")
            DANGER,
        }
    }

    data class Checkbox(
        val actionId: String,
        val options: List<org.straycats.birmancat.api.sender.BlockKit.Option>,
        val initialOptions: List<org.straycats.birmancat.api.sender.BlockKit.Option>? = null,
        val confirm: org.straycats.birmancat.api.sender.BlockKit.ConfirmationDialog? = null,
        val focusOnLoad: Boolean? = null,
    ) : org.straycats.birmancat.api.sender.BlockKit.BlockKitElement {
        val type = "checkboxes"
    }

    data class Image(
        val imageUrl: String,
        val altText: String,
    ) : org.straycats.birmancat.api.sender.BlockKit.BlockKitElement,
        org.straycats.birmancat.api.sender.BlockKit.BlockKitObject {
        val type = "image"
    }

    data class Text(
        val type: org.straycats.birmancat.api.sender.BlockKit.Text.Type,
        val text: String,
        val emoji: Boolean? = null,
        val verbatim: Boolean? = null,
    ) : org.straycats.birmancat.api.sender.BlockKit.BlockKitObject {
        enum class Type {
            @JsonProperty("plain_text")
            PLAIN_TEXT,

            @JsonProperty("mrkdwn")
            MRKDWN,
        }
    }

    data class ConfirmationDialog(
        val title: org.straycats.birmancat.api.sender.BlockKit.Text,
        val text: org.straycats.birmancat.api.sender.BlockKit.Text,
        val confirm: org.straycats.birmancat.api.sender.BlockKit.Text,
        val deny: org.straycats.birmancat.api.sender.BlockKit.Text,
        val style: org.straycats.birmancat.api.sender.BlockKit.ConfirmationDialog.Style? = null,
    ) : org.straycats.birmancat.api.sender.BlockKit.BlockKitObject {
        enum class Style {
            @JsonProperty("primary")
            PRIMARY,

            @JsonProperty("danger")
            DANGER,
        }
    }

    data class Option(
        val text: org.straycats.birmancat.api.sender.BlockKit.Text,
        val value: String,
    ) : org.straycats.birmancat.api.sender.BlockKit.BlockKitObject

    data class OptionGroup(
        val label: org.straycats.birmancat.api.sender.BlockKit.Text,
        val options: List<org.straycats.birmancat.api.sender.BlockKit.Option>,
    ) : org.straycats.birmancat.api.sender.BlockKit.BlockKitObject
}
