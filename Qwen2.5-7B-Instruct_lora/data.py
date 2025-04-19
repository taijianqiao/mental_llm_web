# import json


# def convert_data(input_data):
#     output_data_list = []
#     for item in input_data:
#         conversations = item["conversation"]
#         for conv in conversations:
#             human_message = {
#                 "from": "human",
#                 "value": conv["input"]
#             }
#             gpt_message = {
#                 "from": "gpt",
#                 "value": conv["output"]
#             }
#             output_conversations = [human_message, gpt_message]
#             output_data = {
#                 "conversations": output_conversations,
#                 "source": "gpt",
#                 "score": 5.0
#             }
#             output_data_list.append(output_data)
#     return output_data_list


# try:
#     with open('/data/tjq/llama3_chat/aiwei.json', 'r', encoding='utf-8') as file:
#         input_data = json.load(file)
#     output_data = convert_data(input_data)
#     with open('haishi_mental.json', 'w', encoding='utf-8') as output_file:
#         json.dump(output_data, output_file, indent=2, ensure_ascii=False)
#     print("数据转换完成，结果已保存到 output.json")
# except FileNotFoundError:
#     print("未找到指定的 JSON 文件，请检查文件路径。")
# except json.JSONDecodeError:
#     print("JSON 文件格式错误，请检查文件内容。")
# except Exception as e:
#     print(f"发生未知错误: {e}")
    
import json


def remove_bracketed_content(json_data):
    for item in json_data:
        conversations = item.get('conversations', [])
        for conversation in conversations:
            value = conversation.get('value', '')
            new_value = ""
            in_brackets = False
            for char in value:
                if char == "（":
                    in_brackets = True
                elif char == "）":
                    in_brackets = False
                elif not in_brackets:
                    new_value += char
            conversation['value'] = new_value
    return json_data


try:
    # 读取 JSON 文件
    with open('/data/tjq/llama3_chat/haishi_mental.json', 'r', encoding='utf-8') as file:
        json_data = json.load(file)

    # 处理 JSON 数据
    new_json_data = remove_bracketed_content(json_data)

    # 保存处理后的数据到新文件
    with open('new_data.json', 'w', encoding='utf-8') as new_file:
        json.dump(new_json_data, new_file, ensure_ascii=False, indent=4)

    print("处理完成，新文件已保存为 new_data.json")
except FileNotFoundError:
    print("错误：未找到指定的 JSON 文件。")
except json.JSONDecodeError:
    print("错误：无法解析 JSON 文件内容。")
except Exception as e:
    print(f"发生未知错误：{e}")
    